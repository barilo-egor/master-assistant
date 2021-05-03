package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.comparators.AppointmentComparatorByDateTime;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClosestAppointmentsProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void run(User user){
        List<Appointment> appointments = appointmentService.findAll().stream()
                .filter(appointment -> appointment.isActive() && appointment.isConfirmed())
                .sorted(new AppointmentComparatorByDateTime())
                .limit(5)
                .collect(Collectors.toList());

        if (appointments.isEmpty()){
            messageSender.sendMessage(user.getChatId(), SystemMessage.NO_APPOINTMENTS.getSystemMessage());
            return;
        }

        StringBuilder text = new StringBuilder();

        for(Appointment appointment : appointments){
            text.append(appointment.getDate()).append(", ").append(appointment.getTime().toLocalTime())
                    .append(" - ").append(appointment.getTime().toLocalTime().plusMinutes(appointment.getDuration()))
                    .append("\n");
            if(appointment.getUser().getRole().equals(Role.USER)){
                text.append(appointment.getUser().getFirstName()).append(" ").append(appointment.getUser().getLastName())
                        .append("\n");
                if(appointment.getUser().getPhoneNumber() != null){
                    text.append(user.getPhoneNumber());
                }
            } else text.append(appointment.getComment()).append("(запись мастера)");
            text.append("==========");
        }

        messageSender.sendMessage(user.getChatId(), text.toString());
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
