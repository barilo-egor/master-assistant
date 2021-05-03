package by.moon.masterassistant.util;

import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemConstant;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.LocalDate;

public class CalendarUtil {

    public static final String IGNORE = "ignore!@#$%^&";

    public static final String[] WD = {"ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС"};

    public static ReplyKeyboard generateKeyboard(LocalDate date, Command data) {

        if (date == null) {
            return null;
        }

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // row - Month and Year
        List<InlineKeyboardButton> headerRow = new ArrayList<>();
        headerRow.add(createButton(IGNORE, new SimpleDateFormat("LLLL yyyy", new Locale("ru")).format(date.toDate())));
        keyboard.add(headerRow);

        // row - Days of the week
        List<InlineKeyboardButton> daysOfWeekRow = new ArrayList<>();
        for (String day : WD) {
            daysOfWeekRow.add(createButton(IGNORE, day));
        }
        keyboard.add(daysOfWeekRow);

        LocalDate firstDay = date.dayOfMonth().withMinimumValue();

        int shift = firstDay.dayOfWeek().get() - 1;
        int daysInMonth = firstDay.dayOfMonth().getMaximumValue();
        int rows = ((daysInMonth + shift) % 7 > 0 ? 1 : 0) + (daysInMonth + shift) / 7;
        for (int i = 0; i < rows; i++) {
            keyboard.add(buildRow(firstDay, shift, data));
            firstDay = firstDay.plusDays(7 - shift);
            shift = 0;
        }

        List<InlineKeyboardButton> controlsRow = new ArrayList<>();
        controlsRow.add(createButton(Command.ANOTHER_MONTH.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + date.minusMonths(1), "⬅️"));
        controlsRow.add(createButton(Command.ANOTHER_MONTH.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + date.plusMonths(1), "➡️"));
        keyboard.add(controlsRow);
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton createButton(String callBack, String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setCallbackData(callBack);
        button.setText(text);
        return button;
    }

    private static List<InlineKeyboardButton> buildRow(LocalDate date, int shift, Command data) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        int day = date.getDayOfMonth();
        LocalDate callbackDate = date;
        for (int j = 0; j < shift; j++) {
            row.add(createButton(IGNORE, " "));
        }
        for (int j = shift; j < 7; j++) {
            if (day <= (date.dayOfMonth().getMaximumValue())) {
                row.add(createButton(data + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + callbackDate,
                        Integer.toString(day++)));
                callbackDate = callbackDate.plusDays(1);
            } else {
                row.add(createButton(IGNORE, " "));
            }
        }
        return row;
    }
}
