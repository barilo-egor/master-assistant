package by.moon.masterassistant.util;

import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.SystemConstant;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import org.joda.time.LocalDate;

public final class CallbackHandlerUtil {
    private CallbackHandlerUtil() {
    }

    public static Command getCommand(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        if(data.contains(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())) {
            return Command.fromString(getCommandFromCompositeCallbackData(data));
        } else {
            return Command.fromString(data);
        }
    }

    private static String getCommandFromCompositeCallbackData(String data){
        return data.substring(0, data.indexOf(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant()));
    }

    public static LocalDate parseDate(CallbackQuery callbackQuery, int index){
        return LocalDate.parse(callbackQuery.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[index]);
    }

    public static Integer parseInteger(CallbackQuery callbackQuery, int index){
        return Integer.parseInt(callbackQuery.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[index]);
    }

    public static Long parseLong(CallbackQuery callbackQuery, int index){
        return Long.parseLong(callbackQuery.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[index]);
    }
}
