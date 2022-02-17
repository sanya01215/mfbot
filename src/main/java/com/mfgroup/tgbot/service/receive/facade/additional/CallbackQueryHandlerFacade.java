package com.mfgroup.tgbot.service.receive.facade.additional;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.service.receive.handler.cbquery.CallbackQueryService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.botdata.BotData.UserPositionEnum.START;
@Service
public class CallbackQueryHandlerFacade implements ReceivedObjectHandlerFacade<CallbackQuery> {
    private final UserRepository userRepo;

    private final CallbackQueryService cbQueryService;

    public CallbackQueryHandlerFacade(UserRepository userRepo, CallbackQueryService cbQueryService) {
        this.userRepo = userRepo;
        this.cbQueryService = cbQueryService;
    }
    @Override
    public SendMsgEditMsgAdapter shareReceiveObjToNeededHandler(CallbackQuery cbQuery) {
        Message message = cbQuery.getMessage();
        User user = userRepo.findById(message.getChatId())
                .orElseGet(() -> new User(message.getChatId(), START));
        SendMsgEditMsgAdapter sendMsgEditMsgAdapter =cbQueryService.handleReceivedObj(cbQuery,user.getChatId());
        return sendMsgEditMsgAdapter;
    }
}
