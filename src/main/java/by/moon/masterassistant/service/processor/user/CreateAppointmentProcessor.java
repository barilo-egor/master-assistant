package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateAppointmentProcessor {
    private MessageSender messageSender;
    private AppointmentService appointmentService;
    private UserService userService;
    private UserMenuSender userMenuSender;

    public void requestForApply(User user, CallbackQuery query) {
        LocalDate date = LocalDate.parse(query.getData().split("/")[1]);
        Time time = Time.valueOf(LocalTime.parse(query.getData().split("/")[2]));
        if (!isTimeFree(date, time)) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.TIME_NOT_FREE.getSystemMessage());
            return;
        }

        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());

        String text = SystemMessage.CONFIRM_DATA.getSystemMessage() + "\n" +
                "Дата: " + date + "\n" + "Время: " + time;
        messageSender.sendMessage(user.getChatId(), text,
                KeyboardFactory.getInlineOneRow(
                        new String[]{"Подтвердить", "Отмена"},
                        new String[]{Command.APPLY_APPOINTMENT.getCommand() + "/" + date + "/" + time,
                                Command.CANCEL_APPOINTMENT.getCommand()},
                        InlineType.DATA
                ));
    }

    public Appointment createAppointment(User user, CallbackQuery query) {
        LocalDate date = LocalDate.parse(query.getData().split("/")[1]);
        Time time = Time.valueOf(LocalTime.parse(query.getData().split("/")[2]));

        if(user.getRole().equals(Role.USER)) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.APPOINTMENT_CREATED.getSystemMessage());
            for(User master : userService.findAll().stream()
                    .filter(master -> master.getRole().equals(Role.MASTER))
                    .collect(Collectors.toList())){
                messageSender.sendMessage(master.getChatId(), SystemMessage.NEW_APPOINTMENT.getSystemMessage());
            }

            if(user.getPhoneNumber() == null) {
                userService.changeState(user, UserState.SENDING_CONTACT);
                messageSender.sendMessage(user.getChatId(), SystemMessage.SEND_CONTACT.getSystemMessage(),
                        KeyboardFactory.getReplyContact(Command.SEND_CONTACT.getCommand(),
                                Command.CANCEL.getCommand()));
            } else {
                userMenuSender.send(user.getChatId(), UserMenuType.MAIN);
            }
        }
        Appointment appointment = new Appointment();
        appointment.setActive(true);
        appointment.setConfirmed(false);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setDuration(0);
        appointment.setUser(user);
        appointment = appointmentService.save(appointment);
        user.setBuffer(appointment.getId().toString());
        userService.save(user);
        return appointment;
    }

    private boolean isTimeFree(LocalDate date, Time time) {
        List<Appointment> appointments = appointmentService.findByDate(date).stream()
                .filter(Appointment::isConfirmed).collect(Collectors.toList());
        for (Appointment appointment : appointments) {
            Time appointmentEndTime =
                    Time.valueOf(appointment.getTime().toLocalTime().plusMinutes(appointment.getDuration()));
            if ((time.after(appointment.getTime())
                    && time.before(appointmentEndTime))
                    || time.equals(appointment.getTime())) return false;
        }
        return true;
    }

    @Autowired
    public void setUserMenuSender(UserMenuSender userMenuSender) {
        this.userMenuSender = userMenuSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
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
