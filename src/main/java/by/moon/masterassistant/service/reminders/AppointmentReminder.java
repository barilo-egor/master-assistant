package by.moon.masterassistant.service.reminders;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.service.beanservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Service
public class AppointmentReminder {
    private AppointmentService appointmentService;
    private MessageSender messageSender;
    private UserService userService;

    @Async
    public void run(){
        boolean running = true;
        while(running){
            for(Appointment appointment : appointmentService.findByDate(LocalDate.now()).stream()
                    .filter(Appointment::isActive).filter(Appointment::isConfirmed)
                    .collect(Collectors.toList())){
                LocalTime appointmentTime = appointment.getTime().toLocalTime().plusHours(1);
                LocalTime now = LocalTime.now();
                if(appointmentTime.getHour() == now.getHour() && appointmentTime.getMinute() == now.getMinute()){
                    messageSender.sendMessage(appointment.getUser().getChatId(),
                            SystemMessage.APPOINTMENT_HOUR_REMIND_FOR_USER.getSystemMessage());
                    for(User master : userService.findAll().stream()
                            .filter(user -> user.getRole().equals(Role.MASTER)).collect(Collectors.toList())){
                        String text =  SystemMessage.APPOINTMENT_HOUR_REMIND_FOR_MASTER.getSystemMessage() + "\n"
                                + appointment.getDate().toString() + " - " + appointment.getTime().toString() + "\n"
                                + appointment.getUser().getFirstName() + " " + appointment.getUser().getLastName();

                        if(appointment.getUser().getPhoneNumber() != null)
                            text = text.concat(", " + appointment.getUser().getPhoneNumber());
                        messageSender.sendMessage(master.getChatId(), text);
                    }
                    appointment.setActive(false);
                    appointmentService.save(appointment);
                }
            }
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
