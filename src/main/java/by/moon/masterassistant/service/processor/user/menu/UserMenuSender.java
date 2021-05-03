package by.moon.masterassistant.service.processor.user.menu;

import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.BotMessageType;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.UserMenuType;
import by.moon.masterassistant.service.beanservice.BotMessageService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMenuSender {
    private MessageSender messageSender;
    private BotMessageService botMessageService;

    public void send(long chatId, UserMenuType type) {
        switch (type) {
            case MAIN:
                messageSender.sendMessage(chatId, botMessageService.findByType(BotMessageType.USER_MENU).getText(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.SCHEDULE.getCommand(),
                                Command.MY_APPOINTMENT.getCommand(),
                                Command.CONTACTS.getCommand(),
                                Command.SERVICES_AND_PRICES.getCommand()));
                break;
        }

    }

    @Autowired
    public void setBotMessageService(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
