package by.moon.masterassistant.service.dispatchers;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.service.processor.DeleteEntityProcessor;
import by.moon.masterassistant.util.CallbackHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class AdminCallbackQueryDispatcher {
    private DeleteEntityProcessor deleteEntityProcessor;

    public void dispatch(CallbackQuery callbackQuery, User user){
        Command command = CallbackHandlerUtil.getCommand(callbackQuery);
        switch (command){
            case DELETING_CONTACT:
                deleteEntityProcessor.deleteContact(user, callbackQuery);
                break;
            case DELETING_SERVICE:
                deleteEntityProcessor.deleteService(user, callbackQuery);
                break;
            case DELETING_WORKING_DAY:
                deleteEntityProcessor.deleteWorkingDay(user, callbackQuery);
                break;
        }
    }

    @Autowired
    public void setDeleteEntityProcessor(DeleteEntityProcessor deleteEntityProcessor) {
        this.deleteEntityProcessor = deleteEntityProcessor;
    }
}
