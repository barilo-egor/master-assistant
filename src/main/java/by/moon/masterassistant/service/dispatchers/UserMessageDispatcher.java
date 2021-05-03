package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.admin.menu.AdminMenuSender;
import by.moon.masterassistant.service.processor.master.MasterMenuSender;
import by.moon.masterassistant.service.processor.user.*;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class UserMessageDispatcher {
    private UserMenuSender userMenuSender;
    private GreetingSender greetingSender;
    private ServicesAndPricesProcessor servicesAndPricesProcessor;
    private ContactsProcessor contactsProcessor;
    private AdminMenuSender adminMenuSender;
    private UserService userService;
    private ScheduleProcessor scheduleProcessor;
    private MasterMenuSender masterMenuSender;
    private MessageSender messageSender;
    private MyAppointmentProcessor myAppointmentProcessor;

    public void dispatch(Message message, User user){
        if(user.getUserState() != UserState.START) dispatchByCurrentCommand(message, user);
        else dispatchByNewCommand(message, user);
    }

    private void dispatchByNewCommand(Message message, User user) {
        switch (Command.fromString(message.getText())){
            default:
                if(!isAdminPassword(message, user)) {
                    if(!isMasterPassword(message, user)) {
                        userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
                    } else masterMenuSender.send(user.getChatId(), MasterMenuType.MAIN);
                } else {
                    adminMenuSender.send(user.getChatId(), AdminMenuType.MAIN);
                }
                break;
            case START:
                greetingSender.send(user.getChatId());
                userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
                break;
            case SERVICES_AND_PRICES:
                servicesAndPricesProcessor.run(user, Command.READ_MASTER_SERVICE);
                break;
            case CONTACTS:
                contactsProcessor.run(user);
                break;
            case SCHEDULE:
                scheduleProcessor.run(user, Command.CHOOSE_DATE, LocalDate.now());
                break;
            case MY_APPOINTMENT:
                myAppointmentProcessor.run(user);
                break;
        }
    }

    private boolean isMasterPassword(Message message, User user) {
        if(message.getText().equals(Command.MASTER_PASSWORD.getCommand())){
            user.setRole(Role.MASTER);
            userService.save(user);
            return true;
        } else return false;
    }

    private boolean isAdminPassword(Message message, User user) {
        if(message.getText().equals(Command.ADMIN_PASSWORD.getCommand())){
            user.setRole(Role.ADMIN);
            userService.save(user);
            return true;
        } else return false;
    }

    private void dispatchByCurrentCommand(Message message, User user) {
        if(message.hasText() && message.getText().equals(Command.CANCEL.getCommand())){
            userService.changeState(user, UserState.START);
            userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
            return;
        }
        switch (user.getUserState()){
            case SENDING_CONTACT:
                if(message.hasContact()){
                    user.setPhoneNumber(message.getContact().getPhoneNumber());
                    userService.changeState(user, UserState.START);
                    messageSender.sendMessage(user.getChatId(), SystemMessage.CONTACT_SAVED.getSystemMessage());
                    userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
                } else messageSender.sendMessage(user.getChatId(), SystemMessage.NEED_CONTACT.getSystemMessage());
                break;
        }
    }

    @Autowired
    public void setMyAppointmentProcessor(MyAppointmentProcessor myAppointmentProcessor) {
        this.myAppointmentProcessor = myAppointmentProcessor;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setMasterMenuSender(MasterMenuSender masterMenuSender) {
        this.masterMenuSender = masterMenuSender;
    }

    @Autowired
    public void setScheduleProcessor(ScheduleProcessor scheduleProcessor) {
        this.scheduleProcessor = scheduleProcessor;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAdminMenuSender(AdminMenuSender adminMenuSender) {
        this.adminMenuSender = adminMenuSender;
    }

    @Autowired
    public void setContactsProcessor(ContactsProcessor contactsProcessor) {
        this.contactsProcessor = contactsProcessor;
    }

    @Autowired
    public void setServicesAndPricesProcessor(ServicesAndPricesProcessor servicesAndPricesProcessor) {
        this.servicesAndPricesProcessor = servicesAndPricesProcessor;
    }

    @Autowired
    public void setGreetingSender(GreetingSender greetingSender) {
        this.greetingSender = greetingSender;
    }

    @Autowired
    public void setUserMenuSender(UserMenuSender userMenuSender) {
        this.userMenuSender = userMenuSender;
    }
}
