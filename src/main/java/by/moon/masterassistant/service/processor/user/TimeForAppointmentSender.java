package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.Appointment;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.WorkingDay;
import by.moon.masterassistant.bean.comparators.AppointmentComparatorByTime;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.exceptions.WorkingDayNotFoundException;
import by.moon.masterassistant.service.beanservice.AppointmentService;
import by.moon.masterassistant.service.beanservice.WorkingDayService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeForAppointmentSender {
    private WorkingDayService workingDayService;
    private MessageSender messageSender;
    private AppointmentService appointmentService;

    public void send(User user, CallbackQuery query, Command data) {
        LocalDate date = LocalDate.parse(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]);
        LocalDate now = LocalDate.now();
        if (now.plusDays(1).isAfter(date)) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.DATE_IS_BEFORE.getSystemMessage());
            return;
        }

        if (user.getRole().equals(Role.USER)) {
            for (Appointment appointment : appointmentService.findByUser(user)) {
                if (appointment.isActive()) {
                    messageSender.sendMessage(user.getChatId(), SystemMessage.HAS_ACTIVE_APPOINTMENT.getSystemMessage());
                    return;
                }
            }
        }

        WorkingDay workingDay;
        try {
            workingDay = workingDayService
                    .findByDay(getDayFromDate(LocalDate.parse(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1])));
        } catch (WorkingDayNotFoundException ex) {
            messageSender.sendMessage(user.getChatId(), SystemMessage.REST_DAY.getSystemMessage());
            return;
        }

        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        List<Appointment> appointmentsForDate =
                appointmentService.findByDate(java.time.LocalDate.parse(date.toString())).stream()
                        .sorted(new AppointmentComparatorByTime())
                        .collect(Collectors.toList());
        ;
        StringBuilder text = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("HH:mm");
        text.append(SystemMessage.OPERATING_MODE.getSystemMessage())
                .append(format.format(workingDay.getTimeFrom()))
                .append(" - ").append(format.format(workingDay.getTimeTo()));

        if (appointmentsForDate.size() == 0) {
            text.append("\n").append(SystemMessage.DAY_IS_FREE.getSystemMessage()).append("\n")
                    .append(SystemMessage.CHOOSE_TIME.getSystemMessage());
        } else {
            text.append("\n").append(SystemMessage.APPOINTMENT_LIST.getSystemMessage()).append("\n");
            for (Appointment appointment : appointmentsForDate) {
                if (appointment.isConfirmed()) {
                    text.append("\n").append(appointment.getTime().toLocalTime()).append(" - ")
                            .append(appointment.getTime().toLocalTime().plusMinutes(appointment.getDuration()));
                } else {
                    text.append("\n").append(appointment.getTime().toLocalTime())
                            .append(SystemMessage.NOT_CONFIRMED.getSystemMessage());
                }
                if (!appointment.getUser().getRole().equals(Role.MASTER)) {
                    text.append("\n").append(appointment.getUser().getFirstName()).append(" ")
                            .append(appointment.getUser().getLastName());
                    if (appointment.getUser().getPhoneNumber() != null)
                        text.append(", ").append(appointment.getUser().getPhoneNumber());
                } else {
                    if (appointment.getComment() != null) text.append("\n").append(appointment.getComment());
                    else text.append("\n").append(SystemMessage.MASTER_ADDED_APPOINTMENT.getSystemMessage());
                }
                text.append("\n");
                text.append("----------");
            }
        }
        messageSender.sendMessage(user.getChatId(), text.toString(),
                generateTimeKeyboard(workingDay.getTimeFrom().toLocalTime(), workingDay.getTimeTo().toLocalTime(),
                        date, data));
    }

    private Day getDayFromDate(LocalDate date) {
        return Day.values()[date.getDayOfWeek()];
    }

    public ReplyKeyboard generateTimeKeyboard(LocalTime from, LocalTime to, LocalDate date, Command command) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        while (!from.isAfter(to)) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (from.isAfter(to)) break;
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(from.toString());
                button.setCallbackData(command + "/" + date + "/" + from);
                row.add(button);
                from = from.plusMinutes(30);
            }
            rows.add(row);
        }

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Вернутся");
        button.setCallbackData(Command.BACK_TO_SCHEDULE.getCommand());
        row.add(button);
        rows.add(row);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Autowired
    public void setWorkingDayService(WorkingDayService workingDayService) {
        this.workingDayService = workingDayService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
