package by.moon.masterassistant.util;

import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.Day;
import by.moon.masterassistant.enums.TimeValue;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public final class PreparedKeyboardFactory {
    private PreparedKeyboardFactory() {
    }

    public static InlineKeyboardMarkup getDurationInline(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("30 мин.");
        inlineKeyboardButton1.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 30);
        row1.add(inlineKeyboardButton1);

        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton2.setText("1 час");
        inlineKeyboardButton2.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 60);
        row1.add(inlineKeyboardButton2);
        rows.add(row1);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        inlineKeyboardButton3.setText("1ч.30мин.");
        inlineKeyboardButton3.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 90);
        row2.add(inlineKeyboardButton3);

        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();
        inlineKeyboardButton4.setText("2 часа");
        inlineKeyboardButton4.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 120);
        row2.add(inlineKeyboardButton4);
        rows.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton5 = new InlineKeyboardButton();
        inlineKeyboardButton5.setText("2ч.30мин.");
        inlineKeyboardButton5.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 150);
        row3.add(inlineKeyboardButton5);

        InlineKeyboardButton inlineKeyboardButton6 = new InlineKeyboardButton();
        inlineKeyboardButton6.setText("3 часа");
        inlineKeyboardButton6.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 180);
        row3.add(inlineKeyboardButton6);
        rows.add(row3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton7 = new InlineKeyboardButton();
        inlineKeyboardButton7.setText("3ч.30мин.");
        inlineKeyboardButton7.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 210);
        row4.add(inlineKeyboardButton7);

        InlineKeyboardButton inlineKeyboardButton8 = new InlineKeyboardButton();
        inlineKeyboardButton8.setText("4 часа");
        inlineKeyboardButton8.setCallbackData(Command.CHOOSING_DURATION.getCommand() + "/" + 240);
        row4.add(inlineKeyboardButton8);
        rows.add(row4);

        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public static ReplyKeyboard getTimeReplyTwoRow() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for(int i = TimeValue.FIRST_WORK_HOUR.getValue(); i <= TimeValue.LAST_WORK_HOUR.getValue(); i += 2){
            KeyboardRow row = new KeyboardRow();
            row.add(i + ":00");
            if(i < TimeValue.LAST_WORK_HOUR.getValue()) row.add((i + 1) + ":00");
            keyboardRows.add(row);
        }

        KeyboardRow row = new KeyboardRow();
        row.add(Command.CANCEL.getCommand());
        keyboardRows.add(row);

        keyboard.setKeyboard(keyboardRows);
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }

    public static ReplyKeyboard getDaysReply(){
        return  KeyboardFactory.getReplyTwoRow(Day.MONDAY.getDayName(),
                Day.TUESDAY.getDayName(),
                Day.WEDNESDAY.getDayName(),
                Day.THURSDAY.getDayName(),
                Day.FRIDAY.getDayName(),
                Day.SATURDAY.getDayName(),
                Day.SUNDAY.getDayName(),
                Command.CANCEL.getCommand());
    }

    public static ReplyKeyboard getCancelReply(){
        return KeyboardFactory.getReplyOneRow(Command.CANCEL.getCommand());
    }
}
