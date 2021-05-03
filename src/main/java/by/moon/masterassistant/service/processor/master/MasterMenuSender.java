package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.MasterMenuType;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterMenuSender {
    private MessageSender messageSender;

    public void send(long chatId, MasterMenuType type) {
        switch (type){
            case MAIN:
                messageSender.sendMessage(chatId, SystemMessage.HELLO_MASTER.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.SCHEDULE.getCommand(),
                                Command.NEW_APPOINTMENTS.getCommand(),
                                Command.MONEY_ACCOUNTING.getCommand(),
                                Command.CLOSEST_APPOINTMENTS.getCommand()));
                break;
            case BALANCE_CHANGE:
                messageSender.sendMessage(chatId, SystemMessage.BALANCE_CHANGE_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.DEPOSIT.getCommand(),
                                Command.WITHDRAW.getCommand(),
                                Command.LOAD_STATISTIC.getCommand(),
                                Command.BACK.getCommand()
                        ));
                break;
        }

    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
