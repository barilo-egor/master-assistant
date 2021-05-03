package by.moon.masterassistant.service.processor.admin;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.enums.UserMenuType;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DropAdminProcessor {
    private UserService userService;
    private MessageSender messageSender;
    private UserMenuSender userMenuSender;

    public void run(User user){
        user.setRole(Role.USER);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.ADMIN_OUT.getSystemMessage());
        userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
    }

    @Autowired
    public void setUserMenuSender(UserMenuSender userMenuSender) {
        this.userMenuSender = userMenuSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
