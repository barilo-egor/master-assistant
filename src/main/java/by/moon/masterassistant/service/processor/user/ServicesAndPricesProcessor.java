package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.MasterService;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.BotMessageService;
import by.moon.masterassistant.service.beanservice.MasterServiceService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesAndPricesProcessor {
    private MessageSender messageSender;
    private MasterServiceService masterServiceService;
    private BotMessageService botMessageService;

    public void run(User user, Command dataCommand){
        List<MasterService> masterServices = masterServiceService.findAll();

        if(masterServices.size() == 0){
            messageSender.sendMessage(user.getChatId(), SystemMessage.EMPTY.getSystemMessage());
            return;
        }

        String[] names = new String[masterServices.size()];
        String[] data = new String[masterServices.size()];

        int i = 0;
        for(MasterService masterService : masterServices){
            names[i] = masterService.getName();
            data[i] = dataCommand.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + masterService.getId();
            i++;
        }

        messageSender.sendMessage(user.getChatId(),
                botMessageService.findByType(BotMessageType.SERVICES_AND_PRICES_MENU).getText(),
                KeyboardFactory.getInlineOneRow(names, data, InlineType.DATA));
    }

    @Autowired
    public void setBotMessageService(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }

    @Autowired
    public void setMasterServiceService(MasterServiceService masterServiceService) {
        this.masterServiceService = masterServiceService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
