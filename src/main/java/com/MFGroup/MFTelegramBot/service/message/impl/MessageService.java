package com.MFGroup.MFTelegramBot.service.message.impl;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrap;
import com.MFGroup.MFTelegramBot.factory.keyboard.KeyboardFactory;
import com.MFGroup.MFTelegramBot.factory.message.MessageFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.message.Handler;
import com.MFGroup.MFTelegramBot.service.user.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.*;
import static com.MFGroup.MFTelegramBot.cache.BotData.UserPositionEnum.*;

@Component
public class MessageService implements Handler<Message> {
    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final UserSearch userSearch;

    private final SendMsgEditMsgDecorator sendMessage;

    private User user;

    private final MessageFactory msgFactory;

    public MessageService(KeyboardFactory keyBoard, UserRepository userRepo, UserSearch userSearch, MessageFactory msgFactory) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.userSearch = userSearch;
        this.msgFactory = msgFactory;
        this.sendMessage = new SendMsgWrap();
    }

    @Override
    public SendMsgEditMsgDecorator processReceivedObj(Message message) {
        //id
        int chatId = Math.toIntExact(message.getChatId());
        long chatIdLong = chatId;
        String chatIdStr = String.valueOf(message.getChatId());
        sendMessage.setChatId(chatId);
        ////
        //get user fromDB or make new start user
        user = userRepo.findById(message.getChatId()).orElseGet(() -> new User(chatIdLong, START));
        //set replyMsg
        SendMsgEditMsgDecorator replyMsg = new SendMsgWrap();
        replyMsg.setChatId(chatId);

        switch (user.getPosition()) {
            case START:
                replyMsg = msgFactory.getStartMsg(chatIdStr);
                user.setPosition(ACCEPT);
                break;
            case ACCEPT:
                if (message.getText().equals("Ok")) {
                    user.setPosition(INPUT_FULLNAME);
                    replyMsg = msgFactory.getAcceptMsg(chatIdStr);
                }
                break;
            case INPUT_FULLNAME:
                replyMsg = msgFactory.getInputNameMsg(chatIdStr, saveFullNameAndAskAge(message));
                break;
            case INPUT_AGE:
                replyMsg = msgFactory.getInputAgeMsg(chatIdStr, saveAgeAndAskCity(message));
                break;
            case INPUT_CITY:
                replyMsg = msgFactory.getInputCityMsg(chatIdStr, saveCityAndAskQuiz(message));
                user.setPosition(INPUT_QUIZ);
                break;
            case DONE_REGISTRATION:
                replyMsg = msgFactory.getDoneRegMsg(chatIdStr,finishedRegistrationMenu(message));
                break;
            default:
                throw new RuntimeException("Invalid state. Reg Message class error, method choose");
        }
        userRepo.save(user);
        return replyMsg;
    }

    private String finishedRegistrationMenu(Message message) {
        String msg = message.getText();
        String answer;
        switch (msg) {
            case "Get all users":
                answer = userRepo.findAll().toString();
                break;
            case "Delete all users":
                userRepo.deleteAll();
                answer = userRepo.findAll().toString();
                user = null;
                break;
            case "Find best match user":
                answer = "Your best match user: " + userSearch.findBestTagMatchUser(user.getQuizAnswers(), user.getChatId());
                break;
            default:
                answer = "Command is not correct(End Registration method)";
                break;
        }
        return answer;
    }

    private String saveFullNameAndAskAge(Message message) {
        String answer;
        if (message.hasText()) {
            user.setName(message.getText());
            user.setPosition(INPUT_AGE);
            answer = TYPE_AGE;
        } else {
            answer = INCORRECT;
        }
        return answer;
    }

    private String saveAgeAndAskCity(Message message) {
        String answer;
        try {
            user.setAge(Integer.parseInt(message.getText()));
            user.setPosition(INPUT_CITY);
            answer = TYPE_CITY;
        } catch (IllegalArgumentException e) {
            answer = INCORRECT;
            e.printStackTrace();
        }
        return answer;
    }

    private String saveCityAndAskQuiz(Message message) {
        String answer;
        if (message.hasText()) {
            user.setCity(message.getText());
            answer = NOW_QUIZ;
            user.setPosition(INPUT_QUIZ);
        } else {
            answer = INCORRECT;
        }
        return answer;
    }
}
