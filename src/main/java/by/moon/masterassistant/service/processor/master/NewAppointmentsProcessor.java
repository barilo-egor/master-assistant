package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewAppointmentsProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void run(User user) {
        List<Appointment> appointments = appointmentService.findAll().stream()
                .filter(appointment -> !appointment.isConfirmed() && appointment.isActive()
                        && (appointment.getDate().isAfter(LocalDate.now())
                        || appointment.getDate().isEqual(LocalDate.now())))
                .collect(Collectors.toList());

        for (Appointment appointment : appointments) {
            StringBuilder text = new StringBuilder();
            text.append(appointment.getDate()).append(" ").append(appointment.getTime().toLocalTime()).append("\n");
            if(!appointment.getUser().getRole().equals(Role.MASTER)) {
                text.append(appointment.getUser().getFirstName())
                        .append(" ").append(appointment.getUser().getLastName()).append("\n");
                if (appointment.getUser().getPhoneNumber() != null)
                    text.append(appointment.getUser().getPhoneNumber()).append("\n");
            } else {
                text.append(appointment.getComment()).append("\n");
            }

            messageSender.sendMessage(user.getChatId(), text.toString(),
                    KeyboardFactory.getInlineOneRow(
                            new String[]{"Принять", "Отклонить"},
                            new String[]{Command.CONFIRM_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + appointment.getId(),
                                    Command.REJECT_APPOINTMENT.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + appointment.getId()},
                            InlineType.DATA
                    ));
        }
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
