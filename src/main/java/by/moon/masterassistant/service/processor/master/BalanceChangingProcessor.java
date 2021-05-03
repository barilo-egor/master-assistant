package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.BalanceChange;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.MasterMenuType;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.BalanceChangeService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class BalanceChangingProcessor {
    private MessageSender messageSender;
    private BalanceChangeService balanceChangeService;
    private MasterMenuSender masterMenuSender;
    private UserService userService;

    public void deposit(User user, Message message){
        try{
            BalanceChange balanceChange = new BalanceChange();
            balanceChange.setDeposit(true);
            balanceChange.setAmount(Integer.parseInt(message.getText()));
            balanceChange = balanceChangeService.save(balanceChange);
            user.setBuffer(balanceChange.getId().toString());
            userService.changeState(user, UserState.ENTERING_BALANCE_CHANGE_COMMENT);
            messageSender.sendMessage(user.getChatId(), SystemMessage.ENTER_BALANCE_CHANGE_COMMENT.getSystemMessage(),
                    KeyboardFactory.getReplyOneRow(Command.SKIP.getCommand()));
        } catch (NumberFormatException ex){
            messageSender.sendMessage(user.getChatId(), SystemMessage.WRONG_AMOUNT.getSystemMessage());
        }
    }

    public void withdraw(User user, Message message){
        try{
            BalanceChange balanceChange = new BalanceChange();
            balanceChange.setDeposit(false);
            balanceChange.setAmount(-(Integer.parseInt(message.getText())));
            balanceChangeService.save(balanceChange);
            user.setBuffer(balanceChange.getId().toString());
            userService.changeState(user, UserState.ENTERING_BALANCE_CHANGE_COMMENT);
            messageSender.sendMessage(user.getChatId(), SystemMessage.ENTER_BALANCE_CHANGE_COMMENT.getSystemMessage(),
                    KeyboardFactory.getReplyOneRow(Command.SKIP.getCommand()));
        } catch (NumberFormatException ex){
            messageSender.sendMessage(user.getChatId(), SystemMessage.WRONG_AMOUNT.getSystemMessage());
        }
    }

    public void saveComment(User user, Message message){
        if(!message.getText().equals(Command.SKIP.getCommand())){
            BalanceChange balanceChange = balanceChangeService.findById(Long.parseLong(user.getBuffer()));
            balanceChange.setComment(message.getText());
            balanceChangeService.save(balanceChange);
        }
        userService.changeState(user, UserState.START);
        messageSender.sendMessage(user.getChatId(), SystemMessage.SAVED.getSystemMessage());
        masterMenuSender.send(user.getChatId(), MasterMenuType.BALANCE_CHANGE);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMasterMenuSender(MasterMenuSender masterMenuSender) {
        this.masterMenuSender = masterMenuSender;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setBalanceChangeService(BalanceChangeService balanceChangeService) {
        this.balanceChangeService = balanceChangeService;
    }
}
