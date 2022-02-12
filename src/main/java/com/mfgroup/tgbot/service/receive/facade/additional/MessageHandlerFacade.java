package com.mfgroup.tgbot.service.receive.facade.additional;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.model.user.User;
import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.service.receive.handler.message.AfterRegMsgService;
import com.mfgroup.tgbot.service.receive.handler.message.BeforeRegMsgService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.DONE_REGISTRATION;
import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.START;

@Service
public class MessageHandlerFacade implements ReceivedObjectHandlerFacade<Message> {
    private final UserRepository userRepo;

    private final AfterRegMsgService afterRegServ;

    private final BeforeRegMsgService beforeRegMsgServ;

    public MessageHandlerFacade(UserRepository userRepo, AfterRegMsgService afterRegServ, BeforeRegMsgService beforeRegMsgServ) {
        this.userRepo = userRepo;
        this.afterRegServ = afterRegServ;
        this.beforeRegMsgServ = beforeRegMsgServ;
    }
@Override
    public SendMsgEditMsgAdapter shareReceiveObjToNeededHandler(Message message) {
        User user = userRepo.findById(message.getChatId())
                .orElseGet(() -> new User(message.getChatId(), START));

        return user.getPosition() == DONE_REGISTRATION ?
                afterRegServ.handleReceivedObj(message, user) : beforeRegMsgServ.handleReceivedObj(message, user);
    }

}
