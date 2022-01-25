package com.MFGroup.MFTelegramBot.service.msghandler;

import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.dao.impl.BotData;
import com.MFGroup.MFTelegramBot.decorator.SendMessageEditMessageDecorator;
import com.MFGroup.MFTelegramBot.decorator.SendMessageWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msgsender.MessageSender;
import com.MFGroup.MFTelegramBot.service.userhandler.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class RegMessageHandler implements Handler<Message> {
    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final MessageSender messageSender;

    public final UserSearch userSearch;

    private final SendMessageEditMessageDecorator sendMessage;

    private final Cache<User> cache;

    private User user;

    public RegMessageHandler(KeyboardFactory keyBoard, UserRepository userRepo, MessageSender messageSender, UserSearch userSearch, Cache<User> cache) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.messageSender = messageSender;
        this.userSearch = userSearch;
        this.sendMessage = new SendMessageWrapper();
        this.cache = cache;
    }

    @Override
    public void processReceivedObject(Message message) {
        int chatId = Math.toIntExact(message.getChatId());
        sendMessage.setChatId(chatId);
        user = cache.findById(message.getChatId());
        switch (user.getPosition()) {
            case END_REGISTRATION:
                sendMessage.setText(endRegistration(message));
                sendMessage.setReplyMarkup(keyBoard.getMainReplyKeyBoardMarkup());
                break;
            case INPUT_FULLNAME:
                sendMessage.setText(inputFullName(message));
                break;
            case INPUT_AGE:
                sendMessage.setText(inputAge(message));
                break;
            case INPUT_CITY:
                sendMessage.setText(inputCity(message));
                sendMessage.setReplyMarkup(keyBoard.getRegQuizInlineKeyBoard());
                user.setPosition(BotData.UserPositionEnum.INPUT_QUIZ);
                break;
            default:
                sendMessage.setText("Invalid state. Reg Message class error, method choose");
                break;
        }
        messageSender.sendMessage(sendMessage);
    }

    private String endRegistration(Message message) {
        String msg = message.getText();
        String answer;
        switch (msg) {
            case "Get all users":
                answer = userRepo.findAll().toString();
                break;
            case "Delete all users":
                userRepo.deleteAll();
                answer = userRepo.findAll().toString();
                break;
            case "Find best match user":
                answer = "Your best match user: " + userSearch.findBestTagMatchUser(user.getQuizAnswers(), user.getChatId());
                break;
            default:
                answer = "Command is not correct(End Registration method/Clean cache)";
                cache.removeById(message.getChatId());
                break;
        }
        return answer;
    }

    private String inputFullName(Message message) {
        String answer;
        if (message.hasText()) {
            user.setName(message.getText());
            user.setPosition(BotData.UserPositionEnum.INPUT_AGE);
            answer = "Type your age";
        } else {
            answer = "Name was incorrect. Please type again.";
        }
        return answer;
    }

    private String inputAge(Message message) {
        String answer;
        try {
            user.setAge(Integer.parseInt(message.getText()));
            user.setPosition(BotData.UserPositionEnum.INPUT_CITY);
            answer = "Type your City";
        } catch (IllegalArgumentException e) {
            answer = "Age was incorrect. Please type again.";
            e.printStackTrace();
        }
        return answer;
    }

    private String inputCity(Message message) {
        String answer;
        if (message.hasText()) {
            user.setCity(message.getText());
            answer = "Now quiz. Please, tap all buttons which topic you prefer to conversation\nand then tap <Ok, done it!> : ";
            user.setPosition(BotData.UserPositionEnum.INPUT_QUIZ);
        } else {
            answer = "City was incorrect. Please type again.";
        }
        return answer;
    }
}
