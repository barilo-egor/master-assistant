package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.comparators.AppointmentComparatorByTime;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeleteAppointmentProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void displayAppointment(User user, CallbackQuery query){
        List<Appointment> appointments = appointmentService
                .findByDate(LocalDate.parse(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1])).stream()
                .sorted(new AppointmentComparatorByTime()).collect(Collectors.toList());
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        String[] names = new String[appointments.size() + 1];
        String[] data = new String[appointments.size() + 1];

        int i = 0;
        for(Appointment appointment : appointments){
            names[i] = appointment.getTime().toLocalTime().toString();
            data[i] = Command.DELETING_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + appointment.getId();
            i++;
        }

        names[appointments.size()] = "Назад";
        data[appointments.size()] = Command.DAYS_APPOINTMENTS.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant()
                + query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1];

        messageSender.sendMessage(user.getChatId(), SystemMessage.CHOOSE_APPOINTMENT_FOR_DELETE.getSystemMessage(),
                KeyboardFactory.getInlineOneRow(names, data, InlineType.DATA));
    }

    public void deleteAppointment(User user, CallbackQuery query){
        Appointment appointment = appointmentService.findById(Long.parseLong(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]));
        appointmentService.delete(appointment);
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        messageSender.sendMessage(user.getChatId(), SystemMessage.APPOINTMENT_DELETED.getSystemMessage());
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
}
