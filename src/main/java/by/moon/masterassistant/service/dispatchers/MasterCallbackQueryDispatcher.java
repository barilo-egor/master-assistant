package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.master.ConfirmAppointmentsProcessor;
import by.moon.masterassistant.service.processor.master.DaysAppointmentProcessor;
import by.moon.masterassistant.service.processor.master.DeleteAppointmentProcessor;
import by.moon.masterassistant.service.processor.user.CreateAppointmentProcessor;
import by.moon.masterassistant.service.processor.user.ScheduleProcessor;
import by.moon.masterassistant.service.processor.user.TimeForAppointmentSender;
import by.moon.masterassistant.util.CallbackHandlerUtil;
import by.moon.masterassistant.util.KeyboardFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class MasterCallbackQueryDispatcher {
    private MessageSender messageSender;
    private ScheduleProcessor scheduleProcessor;
    private DaysAppointmentProcessor daysAppointmentProcessor;
    private TimeForAppointmentSender timeForAppointmentSender;
    private CreateAppointmentProcessor createAppointmentProcessor;
    private UserService userService;
    private ConfirmAppointmentsProcessor confirmAppointmentsProcessor;
    private DeleteAppointmentProcessor deleteAppointmentProcessor;

    public void dispatch(CallbackQuery callbackQuery, User user){
        Command command = CallbackHandlerUtil.getCommand(callbackQuery);
        switch (command){
            case ANOTHER_MONTH:
                messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                scheduleProcessor.run(user, Command.DAYS_APPOINTMENTS,
                        CallbackHandlerUtil.parseDate(callbackQuery, 1));
                break;
            case DAYS_APPOINTMENTS:
                daysAppointmentProcessor.run(user, callbackQuery);
                break;
            case ADD_APPOINTMENT:
                timeForAppointmentSender.send(user, callbackQuery, Command.MASTER_ADD_APPOINTMENT);
                break;
            case MASTER_ADD_APPOINTMENT:
                messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                Appointment appointment = createAppointmentProcessor.createAppointment(user, callbackQuery);
                messageSender.sendMessage(user.getChatId(), SystemMessage.ADD_COMMENT.getSystemMessage(),
                        KeyboardFactory.getReplyOneRow(Command.CANCEL.getCommand()));
                user.setBuffer(appointment.getId().toString());
                userService.changeState(user, UserState.ENTERING_COMMENT);
                break;
            case CONFIRM_APPOINTMENT:
                confirmAppointmentsProcessor.askForDuration(user, callbackQuery);
                break;
            case CHOOSING_DURATION:
                confirmAppointmentsProcessor.confirmAppointment(user, callbackQuery);
                break;
            case REJECT_APPOINTMENT:
                confirmAppointmentsProcessor.rejectAppointment(user, callbackQuery);
                break;
            case DELETE_APPOINTMENT:
                deleteAppointmentProcessor.displayAppointment(user, callbackQuery);
                break;
            case DELETING_APPOINTMENT:
                deleteAppointmentProcessor.deleteAppointment(user, callbackQuery);
                break;
            case BACK_TO_SCHEDULE:
                messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                scheduleProcessor.run(user, Command.DAYS_APPOINTMENTS, LocalDate.now());
                break;
        }
    }

    @Autowired
    public void setDeleteAppointmentProcessor(DeleteAppointmentProcessor deleteAppointmentProcessor) {
        this.deleteAppointmentProcessor = deleteAppointmentProcessor;
    }

    @Autowired
    public void setConfirmAppointmentsProcessor(ConfirmAppointmentsProcessor confirmAppointmentsProcessor) {
        this.confirmAppointmentsProcessor = confirmAppointmentsProcessor;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setCreateAppointmentProcessor(CreateAppointmentProcessor createAppointmentProcessor) {
        this.createAppointmentProcessor = createAppointmentProcessor;
    }

    @Autowired
    public void setTimeForAppointmentSender(TimeForAppointmentSender timeForAppointmentSender) {
        this.timeForAppointmentSender = timeForAppointmentSender;
    }

    @Autowired
    public void setDaysAppointmentProcessor(DaysAppointmentProcessor daysAppointmentProcessor) {
        this.daysAppointmentProcessor = daysAppointmentProcessor;
    }

    @Autowired
    public void setScheduleProcessor(ScheduleProcessor scheduleProcessor) {
        this.scheduleProcessor = scheduleProcessor;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
