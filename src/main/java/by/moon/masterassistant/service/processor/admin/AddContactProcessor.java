package by.moon.masterassistant.service.processor.admin;

import by.moon.masterassistant.bean.MasterContact;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.AdminMenuType;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.MasterContactService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.admin.menu.AdminMenuSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class AddContactProcessor {
    private UserService userService;
    private MessageSender messageSender;
    private MasterContactService masterContactService;
    private AdminMenuSender adminMenuSender;

    public void saveNameToBuffer(User user, Message message){
        user.setBuffer(message.getText());
        user.setUserState(UserState.ENTERING_LINK_FOR_CONTACT);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.ENTER_LINK_FOR_CONTACT.getSystemMessage());
    }

    public void createContact(User user, Message message){
        MasterContact masterContact = new MasterContact();
        masterContact.setName(user.getBuffer());
        masterContact.setLink(message.getText());
        masterContactService.save(masterContact);
        userService.changeState(user, UserState.START);
        messageSender.sendMessage(user.getChatId(), SystemMessage.SAVED.getSystemMessage());
        adminMenuSender.send(user.getChatId(), AdminMenuType.CONTACTS_EDIT);
    }

    @Autowired
    public void setAdminMenuSender(AdminMenuSender adminMenuSender) {
        this.adminMenuSender = adminMenuSender;
    }

    @Autowired
    public void setMasterContactService(MasterContactService masterContactService) {
        this.masterContactService = masterContactService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
