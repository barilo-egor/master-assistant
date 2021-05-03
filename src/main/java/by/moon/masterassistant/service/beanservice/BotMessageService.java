package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.BotMessage;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.BotMessageType;
import by.moon.masterassistant.repository.BotMessageRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotMessageService {
    private BotMessageRepository botMessageRepository;
    private MessageSender messageSender;

    public BotMessage save(BotMessage botMessage){
        return botMessageRepository.save(botMessage);
    }

    @SneakyThrows
    public BotMessage findByType(BotMessageType botMessageType){
        return botMessageRepository.findByType(botMessageType)
                .orElseGet(() -> botMessageRepository.save(BotMessage.builder()
                        .type(botMessageType)
                        .text("Значение \"" + botMessageType + "\" не установлено.")
                        .build()
                ));
    }

    public void send(BotMessageType botMessageType, long chatId){
        BotMessage botMessage = findByType(botMessageType);
        if(botMessage.getPhoto() != null) messageSender.sendPhoto(chatId, botMessage.getText(), botMessage.getPhoto());
        else messageSender.sendMessage(chatId, botMessage.getText());
    }

    @Autowired
    public void setBotMessageRepository(BotMessageRepository botMessageRepository) {
        this.botMessageRepository = botMessageRepository;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
