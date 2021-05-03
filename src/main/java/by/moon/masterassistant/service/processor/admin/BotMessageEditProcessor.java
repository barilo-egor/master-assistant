package by.moon.masterassistant.service.processor.admin;

import by.moon.masterassistant.bean.BotMessage;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.BotMessageType;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.exceptions.RequiredStepNotFoundException;
import by.moon.masterassistant.service.beanservice.BotMessageService;
import by.moon.masterassistant.service.beanservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class BotMessageEditProcessor {
    private BotMessageService botMessageService;
    private UserService userService;
    private MessageSender messageSender;

    public void change(User user, Message message){
        BotMessage botMessage;
        switch (user.getUserState()){
            case ENTERING_SERVICE_AND_PRICES_MESSAGE:
                botMessage = botMessageService.findByType(BotMessageType.SERVICES_AND_PRICES_MENU);
                break;
            case ENTERING_USER_MENU_MESSAGE:
                botMessage = botMessageService.findByType(BotMessageType.USER_MENU);
                break;
            case ENTERING_CONTACTS_MESSAGE:
                botMessage = botMessageService.findByType(BotMessageType.CONTACTS_MENU);
                break;
            case ENTERING_GREETING:
                botMessage = botMessageService.findByType(BotMessageType.GREETING);
                break;
            default:
                throw new RequiredStepNotFoundException();
        }
        botMessage.setText(message.getText());
        botMessageService.save(botMessage);

        messageSender.sendMessage(user.getChatId(), SystemMessage.SAVED.getSystemMessage());
        userService.changeState(user, UserState.START);
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setBotMessageService(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
