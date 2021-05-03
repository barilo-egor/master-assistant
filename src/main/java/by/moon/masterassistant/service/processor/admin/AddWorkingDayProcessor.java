package by.moon.masterassistant.service.processor.admin;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.WorkingDay;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.AdminMenuType;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.Day;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.beanservice.WorkingDayService;
import by.moon.masterassistant.service.processor.admin.menu.AdminMenuSender;
import by.moon.masterassistant.util.PreparedKeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Time;

@Service
public class AddWorkingDayProcessor {
    private MessageSender messageSender;
    private UserService userService;
    private WorkingDayService workingDayService;
    private AdminMenuSender adminMenuSender;

    public void saveDayToBuffer(User user, Message message){
        Day day = Day.fromString(message.getText());

        if(day.equals(Day.NONE)) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.WRONG_DAY.getSystemMessage());
            return;
        }

        if (workingDayService.existsByDay(day)){
            messageSender.sendMessage(user.getChatId(), SystemMessage.DAY_EXISTS.getSystemMessage());
            return;
        }

        user.setBuffer(message.getText());
        user.setUserState(UserState.CHOOSING_TIME_FROM_FOR_WORKING_DAY);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.CHOOSE_TIME_FROM.getSystemMessage(),
                PreparedKeyboardFactory.getTimeReplyTwoRow());
    }

    public void saveTimeFromToBuffer(User user, Message message){
        try {
            Time.valueOf(message.getText() + ":00");
        } catch (IllegalArgumentException ex){
            messageSender.sendMessage(user.getChatId(), SystemMessage.WRONG_TIME.getSystemMessage());
            return;
        }

        user.setBuffer(user.getBuffer() + "-" + message.getText());
        user.setUserState(UserState.CHOOSING_TIME_TO_FOR_WORKING_DAY);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.CHOOSE_TIME_TO.getSystemMessage(),
                PreparedKeyboardFactory.getTimeReplyTwoRow());
    }

    public void createWorkingDay(User user, Message message){
        try {
            Time.valueOf(message.getText() + ":00");
        } catch (IllegalArgumentException ex){
            messageSender.sendMessage(user.getChatId(), SystemMessage.WRONG_TIME.getSystemMessage());
            return;
        }
        WorkingDay workingDay = new WorkingDay();
        workingDay.setDay(Day.fromString(user.getBuffer().split("-")[0]));
        workingDay.setTimeFrom(Time.valueOf(user.getBuffer().split("-")[1] + ":00"));
        workingDay.setTimeTo(Time.valueOf(message.getText() + ":00"));
        workingDayService.save(workingDay);
        userService.changeState(user, UserState.START);
        messageSender.sendMessage(user.getChatId(), SystemMessage.SAVED.getSystemMessage());
        adminMenuSender.send(user.getChatId(), AdminMenuType.OPERATING_MODE);
    }

    @Autowired
    public void setAdminMenuSender(AdminMenuSender adminMenuSender) {
        this.adminMenuSender = adminMenuSender;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setWorkingDayService(WorkingDayService workingDayService) {
        this.workingDayService = workingDayService;
    }
}
