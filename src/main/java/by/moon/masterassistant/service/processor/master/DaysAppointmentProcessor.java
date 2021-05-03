package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.comparators.AppointmentComparatorByTime;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DaysAppointmentProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void run(User user, CallbackQuery query) {
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());

        LocalDate date = LocalDate.parse(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]);
        List<Appointment> appointments = appointmentService.findByDate(date).stream()
                .sorted(new AppointmentComparatorByTime())
                .collect(Collectors.toList());
        if (appointments.size() == 0 && (date.isAfter(LocalDate.now()))) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.DAY_IS_FREE.getSystemMessage(),
                    KeyboardFactory.getInlineOneRow(
                            new String[]{"Добавить запись", "Вернуться"},
                            new String[]{Command.ADD_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant()
                                    + date,
                                    Command.BACK_TO_SCHEDULE.getCommand()},
                            InlineType.DATA
                    ));
            return;
        } else if(date.isBefore(LocalDate.now()) || date.isEqual(LocalDate.now())){
            messageSender.sendMessage(user.getChatId(), SystemMessage.DAY_IS_FREE.getSystemMessage(),
                    KeyboardFactory.getInlineOneRow(
                            new String[]{"Вернуться"},
                            new String[]{Command.BACK_TO_SCHEDULE.getCommand()},
                            InlineType.DATA
                    ));
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append(SystemMessage.DAYS_APPOINTMENTS.getSystemMessage()).append(" ").append(date).append("\n");
        for (Appointment appointment : appointments) {
            if(appointment.isConfirmed()) {
                text.append("\n").append(appointment.getTime().toLocalTime()).append(" - ")
                        .append(appointment.getTime().toLocalTime().plusMinutes(appointment.getDuration()));
            } else {
                text.append("\n").append(appointment.getTime().toLocalTime())
                        .append(SystemMessage.NOT_CONFIRMED.getSystemMessage());
            }
            if(!appointment.getUser().getRole().equals(Role.MASTER)){
                text.append("\n").append(appointment.getUser().getFirstName()).append(" ")
                        .append(appointment.getUser().getLastName());
                if(appointment.getUser().getPhoneNumber() != null)
                    text.append(", ").append(appointment.getUser().getPhoneNumber());
            } else {
                if(appointment.getComment() != null) text.append("\n").append(appointment.getComment());
                else text.append("\n").append(SystemMessage.MASTER_ADDED_APPOINTMENT.getSystemMessage());
            }
            text.append("\n");
            text.append("----------");
        }
        messageSender.sendMessage(user.getChatId(), text.toString(),
                KeyboardFactory.getInlineOneRow(
                        new String[]{"Добавить запись", "Удалить запись", "Вернуться"},
                        new String[]{Command.ADD_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + date,
                                Command.DELETE_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + date,
                                Command.BACK_TO_SCHEDULE.getCommand()},
                        InlineType.DATA
                ));
    }

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
