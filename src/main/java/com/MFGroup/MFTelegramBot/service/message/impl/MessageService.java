package com.MFGroup.MFTelegramBot.service.message.impl;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrap;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.message.Handler;
import com.MFGroup.MFTelegramBot.service.user.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.*;
import static com.MFGroup.MFTelegramBot.cache.BotData.UserPositionEnum.*;

@Component
public class MessageService implements Handler<Message> {
    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final UserSearch userSearch;

    private final SendMsgEditMsgDecorator sendMessage;


    private User user;

    public MessageService(KeyboardFactory keyBoard, UserRepository userRepo, UserSearch userSearch) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.userSearch = userSearch;
        this.sendMessage = new SendMsgWrap();
    }

    @Override
    public SendMsgEditMsgDecorator processReceivedObj(Message message) {
        //id
        int chatId = Math.toIntExact(message.getChatId());
        long chatIdLong = chatId;
        String chatIdString = String.valueOf(message.getChatId());
        sendMessage.setChatId(chatId);
        ////
        //get user fromDB or make new start user
        user = userRepo.findById(message.getChatId()).orElseGet(()->new User(chatIdLong,START));
        //set replyMsg
        SendMsgEditMsgDecorator replyMsg = new SendMsgWrap();
        replyMsg.setChatId(chatId);

        switch (user.getPosition()) {
            case START:
                replyMsg = prepareMsg(chatIdString,START_HELLO+"\n"+WAIT_CHOOSE, keyBoard.getOkCancelKB());
                user.setPosition(ACCEPT);
                userRepo.save(user);
                break;

            case ACCEPT:
                if(message.getText().equals("Ok")){
                    user.setPosition(INPUT_FULLNAME);
                    userRepo.save(user);
                    replyMsg = prepareMsg(chatIdString,TYPE_NAME,keyBoard.removeReplyKeyBoard());
                }else {
                    replyMsg = prepareMsg(chatIdString,PROBLEM,keyBoard.removeReplyKeyBoard());
                }
                break;

            case INPUT_FULLNAME:
                replyMsg = prepareMsg(chatIdString,inputFullName(message),keyBoard.removeReplyKeyBoard());
                userRepo.save(user);
                break;
            case INPUT_AGE:
                replyMsg = prepareMsg(chatIdString,inputAge(message),keyBoard.removeReplyKeyBoard());
                userRepo.save(user);
                break;
            case INPUT_CITY:
                replyMsg = prepareMsg(chatIdString,inputCity(message),keyBoard.getRegQuizInlineKeyBoard());
                user.setPosition(INPUT_QUIZ);
                userRepo.save(user);
                break;
            case FINISHED_REGISTRATION:
                replyMsg = prepareMsg(chatIdString,finishedRegistrationMenu(message),keyBoard.getMainKBMarkup());
                break;
            default:
                throw new RuntimeException("Invalid state. Reg Message class error, method choose");
        }
        return replyMsg;
    }

    private  SendMsgEditMsgDecorator prepareMsg(String chatId, String text, ReplyKeyboard replyKeyboard){
        return new SendMsgWrap(chatId,text,replyKeyboard);
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
                user=null;
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

    private String inputFullName(Message message) {
        String answer;
        if (message.hasText()) {
            user.setName(message.getText());
            user.setPosition(INPUT_AGE);
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
            user.setPosition(INPUT_CITY);
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
            answer = "Now quiz. Please, tap all buttons which topic you prefer to conversation\nand then tap <Ok> : ";
            user.setPosition(INPUT_QUIZ);
        } else {
            answer = "City was incorrect. Please type again.";
        }
        return answer;
    }
}
