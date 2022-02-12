package com.mfgroup.tgbot.service.receive.handler.message;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.model.user.User;
import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.model.message.adapter.impl.SendMsgAdapter;
import com.mfgroup.tgbot.service.receive.handler.Handler;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.cache.BotData.MessageHandlerSpeech.*;
import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.*;

@Service
public class BeforeRegMsgService implements Handler<Message> {

    private final UserRepository userRepo;

    public final UserSearch userSearch;

    private final MessageFactory msgFactory;

    public BeforeRegMsgService(UserRepository userRepo, UserSearch userSearch, MessageFactory msgFactory) {
        this.userRepo = userRepo;
        this.userSearch = userSearch;
        this.msgFactory = msgFactory;
    }

    @Override
    public SendMsgEditMsgAdapter handleReceivedObj(Message msg, User user) {
        SendMsgEditMsgAdapter replyMsg = getRegistrationAnswers(msg,user);
        replyMsg.setChatId(String.valueOf(msg.getChatId()));
        userRepo.save(user);
        return replyMsg;
    }

    private SendMsgAdapter getRegistrationAnswers(Message message, User user){
        SendMsgAdapter replyMsg = null;
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
            default:
                throw new RuntimeException("Invalid state. Reg Message class error, method choose");
        }
        replyMsg.setChatId(String.valueOf(message.getChatId()));
        return replyMsg;
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
