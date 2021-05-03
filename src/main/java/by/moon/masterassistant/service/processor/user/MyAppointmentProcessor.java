package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAppointmentProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void run(User user){
        List<Appointment> appointments = appointmentService.findByUser(user);

        if(appointments.isEmpty()){
            messageSender.sendMessage(user.getChatId(), SystemMessage.NO_ACTIVE_APPOINTMENT.getSystemMessage());
            return;
        }

        Appointment appointment = appointments.get(0);
        String text;
        if(appointment.isConfirmed()){
            text = appointment.getDate().toString() + "\n" + appointment.getTime().toLocalTime() +
                    " - " + appointment.getTime().toLocalTime().plusMinutes(appointment.getDuration()) + "\n" +
                    "Статус: подтверждена.";
        } else {
            text = appointment.getDate().toString() + "\n" + appointment.getTime().toLocalTime() + "\n" +
                    "Статус: не подтверждена.";
        }
        messageSender.sendMessage(user.getChatId(), text);
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
