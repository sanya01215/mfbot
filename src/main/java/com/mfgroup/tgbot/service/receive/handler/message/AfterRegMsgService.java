package com.mfgroup.tgbot.service.receive.handler.message;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.service.receive.handler.Handler;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.botdata.BotData.AfterRegMessageHandlerSpeech.*;


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
    @Transactional
    public SendMsgEditMsgAdapter handleReceivedObj(Message message, long userId) {
        User persistUser = userRepo.getById(userId);
        String chatIdStr = String.valueOf(message.getChatId());
        SendMsgEditMsgAdapter sendMsg = msgFactory.getDoneRegMsg(getFinishedRegistrationAnswers(message,persistUser));
        sendMsg.setChatId(chatIdStr);
        return sendMsg;
    }
    private String getFinishedRegistrationAnswers(Message message,User user) {
        String msg = message.getText();
        String answer;
        switch (msg) {
            case GET_USERS:
                answer = userRepo.findAll().toString();
                break;
            case DELETE_ME:
                userRepo.deleteById(user.getChatId());
                answer = userRepo.findAll().toString();
                break;
            case FIND_MATCH_USER:
                User matchUsr= userSearch.findBestTagMatchUser(user.getQuizAnswers(),message.getChatId());
                answer = YOUR_MATCH_USER + matchUsr +"\n"+CAN_CONTACT+matchUsr.getTgUsername();
                break;
            default:
                throw new RuntimeException(INVALID_COMMAND);
        }
        return answer;
    }
}
