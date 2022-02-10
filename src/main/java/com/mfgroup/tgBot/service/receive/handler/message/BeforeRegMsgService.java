package com.mfgroup.tgBot.service.receive.handler.message;

import com.mfgroup.tgBot.dao.UserRepository;
import com.mfgroup.tgBot.factory.keyboard.KeyboardFactory;
import com.mfgroup.tgBot.factory.message.MessageFactory;
import com.mfgroup.tgBot.model.User;
import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgBot.model.decorator.impl.SendMsgWrap;
import com.mfgroup.tgBot.service.receive.handler.Handler;
import com.mfgroup.tgBot.service.user.UserSearch;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgBot.cache.BotData.MessageHandlerSpeech.*;
import static com.mfgroup.tgBot.cache.BotData.UserPositionEnum.*;

@Service
public class BeforeRegMsgService implements Handler<Message> {
    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final UserSearch userSearch;

    private final MessageFactory msgFactory;

    public BeforeRegMsgService(KeyboardFactory keyBoard, UserRepository userRepo, UserSearch userSearch, MessageFactory msgFactory) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.userSearch = userSearch;
        this.msgFactory = msgFactory;
    }

    @Override
    public SendMsgEditMsgDecorator handleReceivedObj(Message msg, User user) {
        SendMsgEditMsgDecorator replyMsg = getRegistrationAnswers(msg,user);
        replyMsg.setChatId(String.valueOf(msg.getChatId()));
        userRepo.save(user);
        return replyMsg;
    }

    private SendMsgWrap getRegistrationAnswers(Message message, User user){
        SendMsgWrap replyMsg = null;
        switch (user.getPosition()) {
            case START:
                replyMsg = msgFactory.getStartMsg();
                user.setPosition(ACCEPT);
                break;
            case ACCEPT:
                if (message.getText().equals("Ok")) {
                    user.setPosition(INPUT_FULLNAME);
                    replyMsg = msgFactory.getAcceptMsg();
                }
                break;
            case INPUT_FULLNAME:
                replyMsg = msgFactory.getInputNameMsg(saveFullNameAndAskAge(message,user));
                break;
            case INPUT_AGE:
                replyMsg = msgFactory.getInputAgeMsg(saveAgeAndAskCity(message,user));
                break;
            case INPUT_CITY:
                replyMsg = msgFactory.getInputCityMsg(saveCityAndAskQuiz(message,user));
                user.setPosition(INPUT_QUIZ);
                break;
            case DONE_REGISTRATION:
                replyMsg = msgFactory.getDoneRegMsg(getFinishedRegistrationAnswers(message,user));
                break;
            default:
                throw new RuntimeException("Invalid state. Reg Message class error, method choose");
        }
        replyMsg.setChatId(String.valueOf(message.getChatId()));
        return replyMsg;
    }
    private String getFinishedRegistrationAnswers(Message message, User user) {
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
                answer = "Your best match user: " + userSearch.findBestTagMatchUser(user.getQuizAnswers(),message.getChatId());
                break;
            default:
                answer = "Command is not correct(End Registration method)";
                break;
        }
        return answer;
    }
    private String saveFullNameAndAskAge(Message message, User user) {
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

    private String saveAgeAndAskCity(Message message, User user) {
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

    private String saveCityAndAskQuiz(Message message, User user) {
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
