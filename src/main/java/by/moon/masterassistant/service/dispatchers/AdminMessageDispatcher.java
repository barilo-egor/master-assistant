package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.DeleteEntityProcessor;
import by.moon.masterassistant.service.processor.admin.*;
import by.moon.masterassistant.service.processor.admin.menu.AdminMenuSender;
import by.moon.masterassistant.service.processor.user.ServicesAndPricesProcessor;
import by.moon.masterassistant.util.PreparedKeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class AdminMessageDispatcher {
    private AdminMenuSender adminMenuSender;
    private DropAdminProcessor dropAdminProcessor;
    private UserService userService;
    private BotMessageEditProcessor botMessageEditProcessor;
    private AddContactProcessor addContactProcessor;
    private AddServiceProcessor addServiceProcessor;
    private AddWorkingDayProcessor addWorkingDayProcessor;
    private DeleteEntityProcessor deleteEntityProcessor;
    private ServicesAndPricesProcessor servicesAndPricesProcessor;

    public void dispatch(Message message, User user){
        if(user.getUserState() != UserState.START) dispatchByCurrentCommand(message, user);
        else dispatchByNewCommand(message, user);
    }

    private void dispatchByNewCommand(Message message, User user) {
        switch (Command.fromString(message.getText())){
            case BACK:
            default:
                adminMenuSender.send(user.getChatId(), AdminMenuType.MAIN);
                break;
            case OUT:
                dropAdminProcessor.run(user);
                break;
            case MESSAGE_EDIT_MENU:
                adminMenuSender.send(user.getChatId(), AdminMenuType.MESSAGE_EDIT);
                break;
            case CONTACTS_EDIT_MENU:
                adminMenuSender.send(user.getChatId(), AdminMenuType.CONTACTS_EDIT);
                break;
            case SERVICES_EDIT_MENU:
                adminMenuSender.send(user.getChatId(), AdminMenuType.SERVICES_EDIT);
                break;
            case OPERATING_MODE_MENU:
                adminMenuSender.send(user.getChatId(), AdminMenuType.OPERATING_MODE);
                break;
            case GREETING_EDIT:
                userService.changeState(user, UserState.ENTERING_GREETING,
                        SystemMessage.ENTER_NEW_GREETING,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case USER_MENU_EDIT:
                userService.changeState(user, UserState.ENTERING_USER_MENU_MESSAGE,
                        SystemMessage.ENTER_NEW_USER_MENU_MESSAGE,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case USER_CONTACTS_MENU_EDIT:
                userService.changeState(user, UserState.ENTERING_CONTACTS_MESSAGE,
                        SystemMessage.ENTER_NEW_CONTACTS_MENU_MESSAGE,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case USER_SERVICES_MENU_EDIT:
                userService.changeState(user, UserState.ENTERING_SERVICE_AND_PRICES_MESSAGE,
                        SystemMessage.ENTER_NEW_SERVICES_AND_PRICES_MESSAGE,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case ADD_CONTACT:
                userService.changeState(user, UserState.ENTERING_NAME_FOR_CONTACT,
                        SystemMessage.ENTER_NAME_FOR_CONTACT,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case ADD_SERVICE:
                userService.changeState(user, UserState.ENTERING_NAME_FOR_SERVICE,
                        SystemMessage.ENTER_NAME_FOR_SERVICE,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case ADD_WORKING_DAY:
                userService.changeState(user, UserState.CHOOSING_DAY_FOR_WORKING_DAY,
                        SystemMessage.CHOOSE_DAY,
                        PreparedKeyboardFactory.getDaysReply());
            case DELETE_CONTACT:
                deleteEntityProcessor.displayContactsForDelete(user);
                break;
            case DELETE_SERVICE:
                servicesAndPricesProcessor.run(user, Command.DELETING_SERVICE);
                break;
            case DELETE_WORKING_DAY:
                deleteEntityProcessor.displayDaysForDelete(user);
                break;
        }

    }

    private void dispatchByCurrentCommand(Message message, User user) {
        if(message.hasText() && message.getText().equals(Command.CANCEL.getCommand())) {
            processCancel(user);
            return;
        }
        switch (user.getUserState()){
            case ENTERING_GREETING:
            case ENTERING_CONTACTS_MESSAGE:
            case ENTERING_USER_MENU_MESSAGE:
            case ENTERING_SERVICE_AND_PRICES_MESSAGE:
                botMessageEditProcessor.change(user, message);
                adminMenuSender.send(user.getChatId(), AdminMenuType.MESSAGE_EDIT);
                break;
            case ENTERING_NAME_FOR_CONTACT:
                addContactProcessor.saveNameToBuffer(user, message);
                break;
            case ENTERING_LINK_FOR_CONTACT:
                addContactProcessor.createContact(user, message);
                break;
            case ENTERING_NAME_FOR_SERVICE:
                addServiceProcessor.saveNameToBuffer(user, message);
                break;
            case SENDING_PHOTO_FOR_SERVICE:
                addServiceProcessor.savePhotoToBuffer(user, message);
                break;
            case ENTERING_DESCRIPTION_FOR_SERVICE:
                addServiceProcessor.saveDescriptionToBuffer(user, message);
                break;
            case ENTERING_PRICE_FOR_SERVICE:
                addServiceProcessor.createMasterService(user, message);
                break;
            case CHOOSING_DAY_FOR_WORKING_DAY:
                addWorkingDayProcessor.saveDayToBuffer(user, message);
                break;
            case CHOOSING_TIME_FROM_FOR_WORKING_DAY:
                addWorkingDayProcessor.saveTimeFromToBuffer(user, message);
                break;
            case CHOOSING_TIME_TO_FOR_WORKING_DAY:
                addWorkingDayProcessor.createWorkingDay(user, message);
                break;
        }
    }

    private void processCancel(User user) {
        switch (user.getUserState()){
            case ENTERING_GREETING:
            case ENTERING_CONTACTS_MESSAGE:
            case ENTERING_USER_MENU_MESSAGE:
            case ENTERING_SERVICE_AND_PRICES_MESSAGE:
                adminMenuSender.send(user.getChatId(), AdminMenuType.MESSAGE_EDIT);
                break;
            case ENTERING_NAME_FOR_SERVICE:
            case SENDING_PHOTO_FOR_SERVICE:
            case ENTERING_PRICE_FOR_SERVICE:
            case ENTERING_DESCRIPTION_FOR_SERVICE:
                adminMenuSender.send(user.getChatId(), AdminMenuType.SERVICES_EDIT);
                break;
            case ENTERING_LINK_FOR_CONTACT:
            case ENTERING_NAME_FOR_CONTACT:
                adminMenuSender.send(user.getChatId(), AdminMenuType.CONTACTS_EDIT);
                break;
            case CHOOSING_DAY_FOR_WORKING_DAY:
            case CHOOSING_TIME_TO_FOR_WORKING_DAY:
            case CHOOSING_TIME_FROM_FOR_WORKING_DAY:
                adminMenuSender.send(user.getChatId(), AdminMenuType.OPERATING_MODE);
                break;
        }
        user.setUserState(UserState.START);
        userService.save(user);
    }

    @Autowired
    public void setServicesAndPricesProcessor(ServicesAndPricesProcessor servicesAndPricesProcessor) {
        this.servicesAndPricesProcessor = servicesAndPricesProcessor;
    }

    @Autowired
    public void setDeleteEntityProcessor(DeleteEntityProcessor deleteEntityProcessor) {
        this.deleteEntityProcessor = deleteEntityProcessor;
    }

    @Autowired
    public void setAddWorkingDayProcessor(AddWorkingDayProcessor addWorkingDayProcessor) {
        this.addWorkingDayProcessor = addWorkingDayProcessor;
    }

    @Autowired
    public void setAddWorkingDay(AddWorkingDayProcessor addWorkingDayProcessor) {
        this.addWorkingDayProcessor = addWorkingDayProcessor;
    }

    @Autowired
    public void setAddServiceProcessor(AddServiceProcessor addServiceProcessor) {
        this.addServiceProcessor = addServiceProcessor;
    }

    @Autowired
    public void setAddContactProcessor(AddContactProcessor addContactProcessor) {
        this.addContactProcessor = addContactProcessor;
    }

    @Autowired
    public void setBotMessageEditProcessor(BotMessageEditProcessor botMessageEditProcessor) {
        this.botMessageEditProcessor = botMessageEditProcessor;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDropAdminProcessor(DropAdminProcessor dropAdminProcessor) {
        this.dropAdminProcessor = dropAdminProcessor;
    }

    @Autowired
    public void setAdminMenuSender(AdminMenuSender adminMenuSender) {
        this.adminMenuSender = adminMenuSender;
    }
}
