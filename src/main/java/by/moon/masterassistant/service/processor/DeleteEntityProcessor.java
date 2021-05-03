package by.moon.masterassistant.service.processor;

import by.moon.masterassistant.bean.MasterContact;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bean.WorkingDay;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.Command;
import by.moon.masterassistant.enums.InlineType;
import by.moon.masterassistant.enums.SystemConstant;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.service.beanservice.MasterContactService;
import by.moon.masterassistant.service.beanservice.MasterServiceService;
import by.moon.masterassistant.service.beanservice.WorkingDayService;
import by.moon.masterassistant.service.processor.user.ServicesAndPricesProcessor;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

@Service
public class DeleteEntityProcessor {
    private MessageSender messageSender;
    private MasterContactService masterContactService;
    private MasterServiceService masterServiceService;
    private WorkingDayService workingDayService;
    private ServicesAndPricesProcessor servicesAndPricesProcessor;

    public void displayContactsForDelete(User user){
        List<MasterContact> masterContacts = masterContactService.findAll();
        String[] names = new String[masterContacts.size()];
        String[] data = new String[masterContacts.size()];

        int i = 0;
        for(MasterContact masterContact : masterContacts){
            names[i] = masterContact.getName();
            data[i] = Command.DELETING_CONTACT + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + masterContact.getId();
            i++;
        }

        messageSender.sendMessage(user.getChatId(), SystemMessage.CHOOSE_CONTACT_FOR_DELETE.getSystemMessage(),
                KeyboardFactory.getInlineOneRow(
                        names, data, InlineType.DATA
                ));
    }

    public void displayDaysForDelete(User user){
        List<WorkingDay> workingDays = workingDayService.findAll();
        String[] names = new String[workingDays.size()];
        String[] data = new String[workingDays.size()];

        int i = 0;
        for(WorkingDay workingDay : workingDays){
            names[i] = workingDay.getDay().getDayName();
            data[i] = Command.DELETING_WORKING_DAY.getCommand() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + workingDay.getId();
            i++;
        }

        messageSender.sendMessage(user.getChatId(), SystemMessage.CHOOSE_WORKING_DAY_FOR_DELETE.getSystemMessage(),
                KeyboardFactory.getInlineOneRow(names, data, InlineType.DATA));
    }

    public void deleteWorkingDay(User user, CallbackQuery query){
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        workingDayService.delete(workingDayService.findById(getValue(query)));
        displayDaysForDelete(user);
    }

    public void deleteContact(User user, CallbackQuery query){
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        masterContactService.delete(masterContactService.findById(getValue(query)));
        displayContactsForDelete(user);
    }

    public void deleteService(User user, CallbackQuery query){
        messageSender.deleteMessage(user.getChatId(), query.getMessage().getMessageId());
        masterServiceService.delete(masterServiceService.findById(getValue(query)));
        servicesAndPricesProcessor.run(user, Command.DELETING_SERVICE);
    }

    private Long getValue(CallbackQuery query){
        return Long.parseLong(query.getData().split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1]);
    }

    @Autowired
    public void setServicesAndPricesProcessor(ServicesAndPricesProcessor servicesAndPricesProcessor) {
        this.servicesAndPricesProcessor = servicesAndPricesProcessor;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setMasterContactService(MasterContactService masterContactService) {
        this.masterContactService = masterContactService;
    }

    @Autowired
    public void setMasterServiceService(MasterServiceService masterServiceService) {
        this.masterServiceService = masterServiceService;
    }

    @Autowired
    public void setWorkingDayService(WorkingDayService workingDayService) {
        this.workingDayService = workingDayService;
    }
}
