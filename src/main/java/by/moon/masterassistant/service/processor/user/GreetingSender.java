package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.enums.BotMessageType;
import by.moon.masterassistant.service.beanservice.BotMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetingSender {
    private BotMessageService botMessageService;

    public void send(long chatId){
        botMessageService.send(BotMessageType.GREETING, chatId);
    }

    @Autowired
    public void setBotMessageService(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }
}
