package com.mfgroup.tgbot.service.receive.main;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.model.User;
import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgbot.service.receive.handler.message.AfterRegMsgService;
import com.mfgroup.tgbot.service.receive.handler.message.BeforeRegMsgService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.DONE_REGISTRATION;
import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.START;

@Service
public class MessageMainHandler implements ReceivedObjectHandler<Message> {
    private final UserRepository userRepo;

    private final AfterRegMsgService afterRegServ;

    private final BeforeRegMsgService beforeRegMsgServ;

    public MessageMainHandler(UserRepository userRepo, AfterRegMsgService afterRegServ, BeforeRegMsgService beforeRegMsgServ) {
        this.userRepo = userRepo;
        this.afterRegServ = afterRegServ;
        this.beforeRegMsgServ = beforeRegMsgServ;
    }
@Override
    public SendMsgEditMsgDecorator shareReceiveObjToNeededHandler(Message message) {
        User user = userRepo.findById(message.getChatId())
                .orElseGet(() -> new User(message.getChatId(), START));

        return user.getPosition() == DONE_REGISTRATION ?
                afterRegServ.handleReceivedObj(message, user) : beforeRegMsgServ.handleReceivedObj(message, user);
    }

}