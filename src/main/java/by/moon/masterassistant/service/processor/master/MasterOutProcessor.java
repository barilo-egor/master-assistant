package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.UserMenuType;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterOutProcessor {
    private UserService userService;
    private UserMenuSender userMenuSender;

    public void run(User user){
        user.setRole(Role.USER);
        userService.save(user);
        userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserMenuSender(UserMenuSender userMenuSender) {
        this.userMenuSender = userMenuSender;
    }
}
