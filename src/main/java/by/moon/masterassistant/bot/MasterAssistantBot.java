package by.moon.masterassistant.bot;

import by.moon.masterassistant.service.dispatchers.UpdateDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@PropertySource("classpath:bot.properties")
public class MasterAssistantBot extends TelegramLongPollingBot {
    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    private UpdateDispatcher updateDispatcher;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()){
            System.out.println(update.getCallbackQuery().getData());
        }
        updateDispatcher.dispatch(update);
    }

    @Autowired
    public void setRoleDispatcher(UpdateDispatcher updateDispatcher) {
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
