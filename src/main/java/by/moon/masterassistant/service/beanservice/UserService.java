package by.moon.masterassistant.service.beanservice;

import by.moon.masterassistant.bean.User;
import by.moon.masterassistant.bot.MessageSender;
import by.moon.masterassistant.enums.UserState;
import by.moon.masterassistant.enums.Role;
import by.moon.masterassistant.enums.SystemMessage;
import by.moon.masterassistant.exceptions.UserNotFoundException;
import by.moon.masterassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private MessageSender messageSender;

    public User findByChatId(Long chatId) throws UserNotFoundException {
        return userRepository.findByChatId(chatId).orElseThrow(UserNotFoundException::new);
    }

    public User create(Chat chat){
        User newUser = new User();
        newUser.setChatId(chat.getId());
        newUser.setUsername(chat.getUserName());
        newUser.setFirstName(chat.getFirstName());
        newUser.setLastName(chat.getLastName());
        newUser.setUserState(UserState.START);
        newUser.setRole(Role.USER);
        return userRepository.save(newUser);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public void changeState(User user, UserState userState){
        user.setUserState(userState);
        save(user);
    }

    public void changeState(User user, UserState userState, SystemMessage systemMessage, ReplyKeyboard keyboard){
        user.setUserState(userState);
        save(user);
        messageSender.sendMessage(user.getChatId(),
                systemMessage.getSystemMessage(),
                keyboard);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
