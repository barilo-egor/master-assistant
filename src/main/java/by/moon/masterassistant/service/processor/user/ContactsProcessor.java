package by.moon.masterassistant.service.processor.user;

import by.moon.masterassistant.bean.MasterContact;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.BotMessageType;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.BotMessageService;
import by.moon.masterassistant.service.beanservice.MasterContactService;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsProcessor {
    private MasterContactService masterContactService;
    private MessageSender messageSender;
    private BotMessageService botMessageService;

    public void run(User user){
        List<MasterContact> masterContacts = masterContactService.findAll();

        if(masterContacts.size() == 0){
            messageSender.sendMessage(user.getChatId(), SystemMessage.EMPTY.getSystemMessage());
            return;
        }

        String[] names = new String[masterContacts.size()];
        String[] urls = new String[masterContacts.size()];

        int i = 0;
        for(MasterContact masterContact : masterContacts){
            names[i] = masterContact.getName();
            urls[i] = masterContact.getLink();
            i++;
        }

        messageSender.sendMessage(user.getChatId(),
                botMessageService.findByType(BotMessageType.CONTACTS_MENU).getText(),
                KeyboardFactory.getInlineOneRow(names, urls, InlineType.URL));
    }

    @Autowired
    public void setBotMessageService(BotMessageService botMessageService) {
        this.botMessageService = botMessageService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setMasterContactService(MasterContactService masterContactService) {
        this.masterContactService = masterContactService;
    }
}
