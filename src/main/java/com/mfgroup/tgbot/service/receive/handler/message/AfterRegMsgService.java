package com.mfgroup.tgbot.service.receive.handler.message;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.model.User;
import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgbot.service.receive.handler.Handler;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
@Service
public class AfterRegMsgService implements Handler<Message> {

    private final UserRepository userRepo;

    public final UserSearch userSearch;

    private final MessageFactory msgFactory;

    public AfterRegMsgService(UserRepository userRepo, UserSearch userSearch, MessageFactory msgFactory) {
        this.userRepo = userRepo;
        this.userSearch = userSearch;
        this.msgFactory = msgFactory;
    }

    @Override
    public SendMsgEditMsgDecorator handleReceivedObj(Message message, User user) {
        String chatIdStr = String.valueOf(message.getChatId());
        SendMsgEditMsgDecorator sendMsg = msgFactory.getDoneRegMsg(getFinishedRegistrationAnswers(message,user));
        sendMsg.setChatId(chatIdStr);
        return sendMsg;
    }
    private String getFinishedRegistrationAnswers(Message message,User user) {
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
                answer = "Your best match user: " + userSearch.findBestTagMatchUser(user.getQuizAnswers(),message.getChatId());
                break;
            default:
                answer = "Command is not correct(End Registration method)";
                break;
        }
        return answer;
    }
}
