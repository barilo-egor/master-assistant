package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.enums.UpdateType;
import by.moon.masterassistant.exceptions.UserNotFoundException;
import by.moon.masterassistant.service.beanservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class UpdateDispatcher {
    private UserService userService;

    private AdminCallbackQueryDispatcher adminCallbackQueryDispatcher;
    private AdminMessageDispatcher adminMessageDispatcher;
    private UserCallbackQueryDispatcher userCallbackQueryDispatcher;
    private UserMessageDispatcher userMessageDispatcher;
    private MasterCallbackQueryDispatcher masterCallbackQueryDispatcher;
    private MasterMessageDispatcher masterMessageDispatcher;

    public void dispatch(Update update){
        User user;
        switch (UpdateType.getUpdateType(update)){
            case MESSAGE:
                user = getUser(update.getMessage().getChat());
                switch (user.getRole()){
                    case USER:
                        userMessageDispatcher.dispatch(update.getMessage(), user);
                        break;
                    case ADMIN:
                        adminMessageDispatcher.dispatch(update.getMessage(), user);
                        break;
                    case MASTER:
                        masterMessageDispatcher.dispatch(update.getMessage(), user);
                        break;
                }
                break;
            case CALLBACK_QUERY:
                user = getUser(update.getCallbackQuery().getMessage().getChat());
                switch (user.getRole()){
                    case USER:
                        userCallbackQueryDispatcher.dispatch(update.getCallbackQuery(), user);
                        break;
                    case ADMIN:
                        adminCallbackQueryDispatcher.dispatch(update.getCallbackQuery(), user);
                        break;
                    case MASTER:
                        masterCallbackQueryDispatcher.dispatch(update.getCallbackQuery(), user);
                        break;
                }
                break;
        }
    }

    private User getUser(Chat chat) {
        try {
            return userService.findByChatId(chat.getId());
        } catch (UserNotFoundException e) {
            e.printStackTrace();
            return userService.create(chat);
        }
    }

    @Autowired
    public void setMasterMessageDispatcher(MasterMessageDispatcher masterMessageDispatcher) {
        this.masterMessageDispatcher = masterMessageDispatcher;
    }

    @Autowired
    public void setMasterCallbackQueryDispatcher(MasterCallbackQueryDispatcher masterCallbackQueryDispatcher) {
        this.masterCallbackQueryDispatcher = masterCallbackQueryDispatcher;
    }

    @Autowired
    public void setAdminCallbackQueryDispatcher(AdminCallbackQueryDispatcher adminCallbackQueryDispatcher) {
        this.adminCallbackQueryDispatcher = adminCallbackQueryDispatcher;
    }

    @Autowired
    public void setAdminMessageDispatcher(AdminMessageDispatcher adminMessageDispatcher) {
        this.adminMessageDispatcher = adminMessageDispatcher;
    }

    @Autowired
    public void setUserCallbackQueryDispatcher(UserCallbackQueryDispatcher userCallbackQueryDispatcher) {
        this.userCallbackQueryDispatcher = userCallbackQueryDispatcher;
    }

    @Autowired
    public void setUserMessageDispatcher(UserMessageDispatcher userMessageDispatcher) {
        this.userMessageDispatcher = userMessageDispatcher;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
