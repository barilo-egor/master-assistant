package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.MasterService;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.service.beanservice.MasterServiceService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class MasterServiceSender {
    private MessageSender messageSender;
    private MasterServiceService masterServiceService;

    public void send(User user, CallbackQuery query){

        MasterService masterService = masterServiceService.findById(Long.parseLong(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]));

        StringBuilder text = new StringBuilder();
        text.append("Услуга: ").append(masterService.getName()).append("\n\n")
                .append(masterService.getDescription());
        if(!masterService.getPrice().equals("none")) text.append("\n\n")
                .append("Цена: ").append(masterService.getPrice());


        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        if(!masterService.getPhoto().equals("none")){
            messageSender.sendPhoto(user.getChatId(), text.toString(), masterService.getPhoto(),
                    KeyboardFactory.getInlineOneRow(
                            new String[] {Command.BACK.getCommand()},
                            new String[] {Command.BACK_TO_SERVICES_AND_PRICES.getCommand()},
                            InlineType.DATA
                    ));
        } else {
            messageSender.sendMessage(user.getChatId(), text.toString(),
                    KeyboardFactory.getInlineOneRow(
                            new String[] {Command.BACK.getCommand()},
                            new String[] {Command.BACK_TO_SERVICES_AND_PRICES.getCommand()},
                            InlineType.DATA
                    ));
        }
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setMasterServiceService(MasterServiceService masterServiceService) {
        this.masterServiceService = masterServiceService;
    }
}
