package by.moon.masterassistant.service.processor.admin;

import by.moon.masterassistant.bean.MasterService;
import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.*;
import by.moon.masterassistant.service.beanservice.MasterServiceService;
import by.moon.masterassistant.service.beanservice.UserService;
import by.moon.masterassistant.service.processor.admin.menu.AdminMenuSender;
import by.moon.masterassistant.util.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.Comparator;
import java.util.List;

@Service
public class AddServiceProcessor {
    private MasterServiceService masterServiceService;
    private MessageSender messageSender;
    private UserService userService;
    private AdminMenuSender adminMenuSender;

    public void saveNameToBuffer(User user, Message message){
        user.setBuffer(message.getText());
        user.setUserState(UserState.SENDING_PHOTO_FOR_SERVICE);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.SEND_PHOTO_FOR_SERVICE.getSystemMessage(),
                KeyboardFactory.getReplyOneRow(Command.SKIP.getCommand(), Command.CANCEL.getCommand()));
    }

    public void savePhotoToBuffer(User user, Message message){
        if(message.hasPhoto()){
            List<PhotoSize> photos = message.getPhoto();
            String photo = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            user.setBuffer(user.getBuffer() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + photo);
        } else if(message.getText().equals(Command.SKIP.getCommand())) user.setBuffer(user.getBuffer() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + "none");
        else {
            messageSender.sendMessage(user.getChatId(), SystemMessage.NEED_PHOTO.getSystemMessage(),
                    KeyboardFactory.getReplyOneRow(Command.SKIP.getCommand(), Command.CANCEL.getCommand()));
            return;
        }
        user.setUserState(UserState.ENTERING_DESCRIPTION_FOR_SERVICE);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.ENTER_DESCRIPTION_FOR_SERVICE.getSystemMessage(),
                KeyboardFactory.getReplyOneRow(Command.BACK.getCommand()));
    }

    public void saveDescriptionToBuffer(User user, Message message){
        user.setBuffer(user.getBuffer() + SystemConstant.CALLBACK_DATA_DELIMITER.getConstant() + message.getText());
        user.setUserState(UserState.ENTERING_PRICE_FOR_SERVICE);
        userService.save(user);
        messageSender.sendMessage(user.getChatId(), SystemMessage.ENTER_PRICE_FOR_SERVICE.getSystemMessage(),
                KeyboardFactory.getReplyOneRow(Command.SKIP.getCommand(), Command.CANCEL.getCommand()));
    }

    public void createMasterService(User user, Message message){
        String price;
        if(!message.getText().equals(Command.SKIP.getCommand())) price = message.getText();
        else price = "none";

        String buffer = user.getBuffer();

        String name = buffer.split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[0];
        String photo = buffer.split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[1];
        String description = buffer.split(SystemConstant.CALLBACK_DATA_DELIMITER.getConstant())[2];

        MasterService masterService = new MasterService();
        masterService.setName(name);
        masterService.setPhoto(photo);
        masterService.setDescription(description);
        masterService.setPrice(price);
        masterServiceService.save(masterService);
        messageSender.sendMessage(user.getChatId(), SystemMessage.SAVED.getSystemMessage());
        userService.changeState(user, UserState.START);
        adminMenuSender.send(user.getChatId(), AdminMenuType.SERVICES_EDIT);
    }

    @Autowired
    public void setAdminMenuSender(AdminMenuSender adminMenuSender) {
        this.adminMenuSender = adminMenuSender;
    }

    @Autowired
    public void setMasterServiceService(MasterServiceService masterServiceService) {
        this.masterServiceService = masterServiceService;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
