package com.mfgroup.tgbot.service.receive.handler.message;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.adapter.message.impl.SendMsgAdapter;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.service.receive.handler.Handler;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.botdata.BotData.AfterRegMessageHandlerSpeech.INVALID_COMMAND;
import static com.mfgroup.tgbot.botdata.BotData.BeforeRegMessageHandlerSpeech.*;
import static com.mfgroup.tgbot.botdata.BotData.UserPositionEnum.*;

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
    @Transactional
    public SendMsgEditMsgAdapter handleReceivedObj(Message msg, long userId) {
        User persistUser = userRepo.getById(userId);
        SendMsgEditMsgAdapter replyMsg = getRegistrationAnswers(msg,persistUser);
        replyMsg.setChatId(String.valueOf(msg.getChatId()));
        return replyMsg;
    }

    private SendMsgAdapter getRegistrationAnswers(Message message, User persistUser){
        SendMsgAdapter replyMsg = null;
        switch (persistUser.getPosition()) {
            case START:
                replyMsg = msgFactory.getStartMsg();
                persistUser.setPosition(ACCEPT);
                break;
            case ACCEPT:
                if (message.getText().equals("Ok")) {
                    persistUser.setPosition(INPUT_FULLNAME);
                    replyMsg = msgFactory.getAcceptMsg();
                }
                break;
            case INPUT_FULLNAME:
                replyMsg = msgFactory.getInputNameMsg(saveFullNameAndAskAge(message,persistUser));
                break;
            case INPUT_AGE:
                replyMsg = msgFactory.getInputAgeMsg(saveAgeAndAskCity(message,persistUser));
                break;
            case INPUT_CITY:
                replyMsg = msgFactory.getInputCityMsg(saveCityAndAskQuiz(message,persistUser));
                persistUser.setPosition(INPUT_QUIZ);
                break;
            default:
                throw new RuntimeException(INVALID_COMMAND);
        }
        assert replyMsg != null;
        replyMsg.setChatId(String.valueOf(message.getChatId()));
        return replyMsg;
    }
    private String saveFullNameAndAskAge(Message message, User user) {
        String answer;
        if (message.hasText()) {
            saveInfoAboutUserFromMsg(message,user);
            answer = TYPE_AGE;
        } else {
            answer = INCORRECT;
        }
        return answer;
    }

    private void saveInfoAboutUserFromMsg(Message message, User persistUser){
        Chat msgChat = message.getChat();
        persistUser.setName(message.getText());
        persistUser.setTgUsername(msgChat.getUserName());
        persistUser.setTgFirstName(msgChat.getFirstName());
        persistUser.setTgLastName(msgChat.getLastName());
        persistUser.setTgBio(msgChat.getBio());
        persistUser.setPosition(INPUT_AGE);
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
