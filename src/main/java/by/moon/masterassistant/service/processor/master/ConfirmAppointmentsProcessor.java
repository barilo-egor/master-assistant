package by.moon.masterassistant.service.processor.master;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import by.moon.masterassistant.util.PreparedKeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class ConfirmAppointmentsProcessor {
    private AppointmentService appointmentService;
    private MessageSender messageSender;
    private UserMenuSender userMenuSender;
    private UserService userService;

    public void askForDuration(User user, CallbackQuery query){
        user.setBuffer(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]);
        userService.save(user);
        messageSender.editMessage(user.getChatId(), query.getMessage().getMessageId(),
                SystemMessage.CHOOSE_DURATION.getSystemMessage(),
                PreparedKeyboardFactory.getDurationInline());
    }

    public void confirmAppointment(User user, CallbackQuery query){
        Appointment appointment = appointmentService.findById(Long.parseLong(user.getBuffer()));
        appointment.setConfirmed(true);
        appointment.setDuration(Integer.parseInt(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]));
        appointmentService.save(appointment);
        if(appointment.getUser().getRole().equals(Role.USER)) {
            String text = SystemMessage.MASTER_CONFIRMED_APPOINTMENT.getSystemMessage() + "\n"
                    + "Дата: " + appointment.getDate() + "\n"
                    + "Время: " + appointment.getTime();
            messageSender.sendMessage(appointment.getUser().getChatId(), text);
        }
        messageSender.editMessage(user.getChatId(), query.getMessage().getMessageId(),
                SystemMessage.SAVED.getSystemMessage());
    }

    public void rejectAppointment(User user, CallbackQuery query){
        Appointment appointment = appointmentService.findById(Long.parseLong(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]));
        String text = SystemMessage.MASTER_REJECTED_APPOINTMENT.getSystemMessage() + "\n"
                + "Дата: " + appointment.getDate() + "\n"
                + "Время: " + appointment.getTime();
        appointmentService.delete(appointment);
        if(appointment.getUser().getRole().equals(Role.USER)) {
            messageSender.sendMessage(appointment.getUser().getChatId(), text);
        }
        messageSender.editMessage(user.getChatId(), query.getMessage().getMessageId(),
                SystemMessage.SAVED.getSystemMessage());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserMenuSender(UserMenuSender userMenuSender) {
        this.userMenuSender = userMenuSender;
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
