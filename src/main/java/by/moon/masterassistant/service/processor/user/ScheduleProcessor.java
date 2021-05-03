package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.util.CalendarUtil;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleProcessor {
    private MessageSender messageSender;

    public void run(User user, Command command, LocalDate date){
        messageSender.sendMessage(user.getChatId(), SystemMessage.SCHEDULE.getSystemMessage(),
                CalendarUtil.generateKeyboard(date, command));
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
