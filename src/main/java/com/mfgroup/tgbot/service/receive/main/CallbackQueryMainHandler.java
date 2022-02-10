package com.mfgroup.tgbot.service.receive.main;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.model.User;
import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgbot.service.receive.handler.cbquery.CallbackQueryService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.START;
@Service
public class CallbackQueryMainHandler implements ReceivedObjectHandler<CallbackQuery> {
    private final UserRepository userRepo;

    private final CallbackQueryService cbQueryService;

    public CallbackQueryMainHandler(UserRepository userRepo, CallbackQueryService cbQueryService) {
        this.userRepo = userRepo;
        this.cbQueryService = cbQueryService;
    }
    @Override
    public SendMsgEditMsgDecorator shareReceiveObjToNeededHandler(CallbackQuery cbQuery) {
        Message message = cbQuery.getMessage();
        User user = userRepo.findById(message.getChatId())
                .orElseGet(() -> new User(message.getChatId(), START));
        SendMsgEditMsgDecorator sendMsgEditMsgDecorator =cbQueryService.handleReceivedObj(cbQuery,user);
        return sendMsgEditMsgDecorator;
    }
}
