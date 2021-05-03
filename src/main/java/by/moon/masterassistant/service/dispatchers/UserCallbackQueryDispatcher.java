package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.processor.user.*;
import by.moon.masterassistant.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import org.joda.time.LocalDate;

@Service
public class UserCallbackQueryDispatcher {
    private MasterServiceSender masterServiceSender;
    private ServicesAndPricesProcessor servicesAndPricesProcessor;
    private MessageSender messageSender;
    private TimeForAppointmentSender timeForAppointmentSender;
    private CreateAppointmentProcessor createAppointmentProcessor;
    private ScheduleProcessor scheduleProcessor;

    public void dispatch(CallbackQuery callbackQuery, User user){
        switch (Command.fromString(callbackQuery.getData())){
            default:
                if(callbackQuery.getData().startsWith(Command.READ_MASTER_SERVICE.getCommand())){
                    masterServiceSender.send(user, callbackQuery);
                } else if(callbackQuery.getData().startsWith(Command.ANOTHER_MONTH.getCommand())){
                    messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                    messageSender.sendMessage(user.getChatId(), SystemMessage.SCHEDULE.getSystemMessage(),
                            CalendarUtil.generateKeyboard(
                                    LocalDate.parse(callbackQuery.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]), Command.CHOOSE_DATE));
                } else if(callbackQuery.getData().startsWith(Command.CHOOSE_DATE.getCommand())){
                    timeForAppointmentSender.send(user, callbackQuery, Command.CREATE_APPOINTMENT);
                } else if (callbackQuery.getData().startsWith(Command.CREATE_APPOINTMENT.getCommand())){
                    createAppointmentProcessor.requestForApply(user, callbackQuery);
                } else if (callbackQuery.getData().startsWith(Command.APPLY_APPOINTMENT.getCommand())){
                    createAppointmentProcessor.createAppointment(user, callbackQuery);
                }
                break;
            case BACK_TO_SERVICES_AND_PRICES:
                messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                servicesAndPricesProcessor.run(user, Command.READ_MASTER_SERVICE);
                break;
            case BACK_TO_SCHEDULE:
            case CANCEL_APPOINTMENT:
                messageSender.deleteMessage(user.getChatId(), callbackQuery.getMessage().getMessageId());
                scheduleProcessor.run(user, Command.CHOOSE_DATE, LocalDate.now());
                break;
        }
    }

    @Autowired
    public void setScheduleProcessor(ScheduleProcessor scheduleProcessor) {
        this.scheduleProcessor = scheduleProcessor;
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
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setServicesAndPricesProcessor(ServicesAndPricesProcessor servicesAndPricesProcessor) {
        this.servicesAndPricesProcessor = servicesAndPricesProcessor;
    }

    @Autowired
    public void setMasterServiceSender(MasterServiceSender masterServiceSender) {
        this.masterServiceSender = masterServiceSender;
    }
}
