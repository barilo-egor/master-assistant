package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.master.*;
import by.moon.masterassistant.service.processor.user.ScheduleProcessor;
import by.moon.masterassistant.service.processor.user.menu.UserMenuSender;
import by.moon.masterassistant.util.KeyboardFactory;
import by.moon.masterassistant.util.PreparedKeyboardFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MasterMessageDispatcher {
    private MasterMenuSender masterMenuSender;
    private ScheduleProcessor scheduleProcessor;
    private UserService userService;
    private AppointmentService appointmentService;
    private NewAppointmentsProcessor newAppointmentsProcessor;
    private BalanceChangingProcessor balanceChangingProcessor;
    private LoadStatistic loadStatistic;
    private ClosestAppointmentsProcessor closestAppointmentsProcessor;

    public void dispatch(Message message, User user){
        if(user.getUserState() != UserState.START) dispatchByCurrentCommand(message, user);
        else dispatchByNewCommand(message, user);
    }

    private void dispatchByNewCommand(Message message, User user) {
        switch (Command.fromString(message.getText())){
            case START:
            default:
                masterMenuSender.send(user.getChatId(), MasterMenuType.MAIN);
                break;
            case SCHEDULE:
                scheduleProcessor.run(user, Command.DAYS_APPOINTMENTS, LocalDate.now());
                break;
            case MASTER_OUT:

                break;
            case NEW_APPOINTMENTS:
                newAppointmentsProcessor.run(user);
                break;
            case DEPOSIT:
                userService.changeState(user, UserState.ENTERING_DEPOSIT, SystemMessage.ENTER_SUM,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case WITHDRAW:
                userService.changeState(user, UserState.ENTERING_WITHDRAW, SystemMessage.ENTER_SUM,
                        PreparedKeyboardFactory.getCancelReply());
                break;
            case MONEY_ACCOUNTING:
                masterMenuSender.send(user.getChatId(), MasterMenuType.BALANCE_CHANGE);
                break;
            case LOAD_STATISTIC:
                loadStatistic.run(user);
                break;
            case CLOSEST_APPOINTMENTS:
                closestAppointmentsProcessor.run(user);
                break;
        }
    }

    private void dispatchByCurrentCommand(Message message, User user) {
        if(message.hasText() && message.getText().equals(Command.CANCEL.getCommand())){
            userService.changeState(user, UserState.START);
            masterMenuSender.send(user.getChatId(), MasterMenuType.MAIN);
            return;
        }
        switch (user.getUserState()){
            case ENTERING_COMMENT:
                Appointment appointment = appointmentService.findById(Long.parseLong(user.getBuffer()));
                appointment.setComment(message.getText());
                appointmentService.save(appointment);
                userService.changeState(user, UserState.START);
                masterMenuSender.send(user.getChatId(), MasterMenuType.MAIN);
                break;
            case ENTERING_DEPOSIT:
                balanceChangingProcessor.deposit(user, message);
                break;
            case ENTERING_WITHDRAW:
                balanceChangingProcessor.withdraw(user, message);
                break;
            case ENTERING_BALANCE_CHANGE_COMMENT:
                balanceChangingProcessor.saveComment(user, message);
                break;
        }
    }

    @Autowired
    public void setClosestAppointmentsProcessor(ClosestAppointmentsProcessor closestAppointmentsProcessor) {
        this.closestAppointmentsProcessor = closestAppointmentsProcessor;
    }

    @Autowired
    public void setLoadStatistic(LoadStatistic loadStatistic) {
        this.loadStatistic = loadStatistic;
    }

    @Autowired
    public void setBalanceChangingProcessor(BalanceChangingProcessor balanceChangingProcessor) {
        this.balanceChangingProcessor = balanceChangingProcessor;
    }

    @Autowired
    public void setNewAppointmentsProcessor(NewAppointmentsProcessor newAppointmentsProcessor) {
        this.newAppointmentsProcessor = newAppointmentsProcessor;
    }

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setScheduleProcessor(ScheduleProcessor scheduleProcessor) {
        this.scheduleProcessor = scheduleProcessor;
    }

    @Autowired
    public void setMasterMenuSender(MasterMenuSender masterMenuSender) {
        this.masterMenuSender = masterMenuSender;
    }
}
