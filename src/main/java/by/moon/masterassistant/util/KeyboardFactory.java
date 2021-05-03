package by.moon.masterassistant.util;

import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.TimeValue;
import by.moon.masterassistant.exceptions.InlineKeyboardException;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.K;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class KeyboardFactory {
    private KeyboardFactory() {
    }

    @SneakyThrows
    public static InlineKeyboardMarkup getInlineOneRow(String[] text, String[] data, InlineType inlineType){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if(text.length != data.length) throw new InlineKeyboardException();
        for(int i = 0; i < text.length; i++){
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(text[i]);
            if(inlineType.equals(InlineType.DATA)) inlineKeyboardButton.setCallbackData(data[i]);
            else if(inlineType.equals(InlineType.URL)) inlineKeyboardButton.setUrl(data[i]);
            row.add(inlineKeyboardButton);
            rows.add(row);
        }
        keyboard.setKeyboard(rows);
        return keyboard;
    }

    public static InlineKeyboardMarkup getInlineOneRow(Map<String, String> map, InlineType inlineType){
        String[] text = new String[map.size()];
        String[] data = new String[map.size()];
        int i = 0;
        for(Map.Entry<String, String> entry : map.entrySet()){
            text[i] = entry.getKey();
            data[i] = entry.getValue();
            i++;
        }
        return getInlineOneRow(text, data, inlineType);
    }

    @SneakyThrows
    public static InlineKeyboardMarkup getInlineOneRow(String... input){
        return getInlineOneRow(input, input, InlineType.DATA);
    }

    public static ReplyKeyboard getReplyOneRow(String... buttons) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for(String button : buttons){
            KeyboardRow row = new KeyboardRow();
            row.add(button);
            keyboardRows.add(row);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }

    public static ReplyKeyboard getReplyContact(String contactButton, String simpleButton){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setText(contactButton);
        button.setRequestContact(true);
        row1.add(button);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(simpleButton);

        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboard getReplyTwoRow(String... buttons) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for(int i = 0; i < buttons.length; i += 2){
            KeyboardRow row = new KeyboardRow();
            row.add(buttons[i]);
            if(i < buttons.length - 1) row.add(buttons[i + 1]);
            keyboardRows.add(row);
        }

        keyboard.setKeyboard(keyboardRows);
        keyboard.setResizeKeyboard(true);
        return keyboard;
    }

    public static InlineKeyboardMarkup getSwitchInline(String text, String data){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setSwitchInlineQuery(data);
        row.add(inlineKeyboardButton);
        rows.add(row);
        keyboard.setKeyboard(rows);
        return keyboard;
    }
}
