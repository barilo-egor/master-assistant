package by.moon.masterassistant.service.processor.admin.menu;

import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.AdminMenuType;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminMenuSender {
    private MessageSender messageSender;

    public void send(long chatId, AdminMenuType type) {
        switch (type) {
            case MAIN:
                messageSender.sendMessage(chatId, SystemMessage.ADMIN_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.MESSAGE_EDIT_MENU.getCommand(),
                                Command.CONTACTS_EDIT_MENU.getCommand(),
                                Command.SERVICES_EDIT_MENU.getCommand(),
                                Command.OPERATING_MODE_MENU.getCommand(),
                                Command.OUT.getCommand()));
                break;
            case MESSAGE_EDIT:
                messageSender.sendMessage(chatId, SystemMessage.MESSAGE_EDIT_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.GREETING_EDIT.getCommand(),
                                Command.USER_MENU_EDIT.getCommand(),
                                Command.USER_CONTACTS_MENU_EDIT.getCommand(),
                                Command.USER_SERVICES_MENU_EDIT.getCommand(),
                                Command.BACK.getCommand()));
                break;
            case CONTACTS_EDIT:
                messageSender.sendMessage(chatId, SystemMessage.CONTACTS_EDIT_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.ADD_CONTACT.getCommand(),
                                Command.DELETE_CONTACT.getCommand(),
                                Command.BACK.getCommand()));
                break;
            case SERVICES_EDIT:
                messageSender.sendMessage(chatId, SystemMessage.SERVICES_EDIT_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.ADD_SERVICE.getCommand(),
                                Command.DELETE_SERVICE.getCommand(),
                                Command.BACK.getCommand()));
                break;
            case OPERATING_MODE:
                messageSender.sendMessage(chatId, SystemMessage.OPERATING_MODE_EDIT_MENU.getSystemMessage(),
                        KeyboardFactory.getReplyTwoRow(
                                Command.ADD_WORKING_DAY.getCommand(),
                                Command.DELETE_WORKING_DAY.getCommand(),
                                Command.BACK.getCommand()));
                break;
        }

    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
