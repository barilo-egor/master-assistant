package by.moon.masterassistant.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Service
public class MessageSender {
    private MasterAssistantBot masterAssistantBot;

    public void sendMessage(long chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        try {
            masterAssistantBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(long chatId, String text, ReplyKeyboard replyKeyboard){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(replyKeyboard);
        try {
            masterAssistantBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(long chatId, String text, String photo){
        try {
            masterAssistantBot.execute(
                    SendPhoto.builder()
                            .chatId(String.valueOf(chatId))
                            .caption(text)
                            .photo(new InputFile(photo))
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(long chatId, String text, String photo, ReplyKeyboard replyKeyboard){
        try {
            masterAssistantBot.execute(
                    SendPhoto.builder()
                            .chatId(String.valueOf(chatId))
                            .caption(text)
                            .photo(new InputFile(photo))
                            .replyMarkup(replyKeyboard)
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(long chatId, int messageId){
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(String.valueOf(chatId));
        try {
            masterAssistantBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessage(long chatId, int messageId, String text, InlineKeyboardMarkup keyboard){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setReplyMarkup(keyboard);
        try {
            masterAssistantBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessage(long chatId, int messageId, String text){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setText(text);
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        try {
            masterAssistantBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(long chatId, File file){
        try {
            masterAssistantBot.execute(
                    SendDocument.builder()
                            .chatId(String.valueOf(chatId))
                            .document(new InputFile(file))
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Autowired
    public void setCheaterBot(MasterAssistantBot masterAssistantBot) {
        this.masterAssistantBot = masterAssistantBot;
    }
}
