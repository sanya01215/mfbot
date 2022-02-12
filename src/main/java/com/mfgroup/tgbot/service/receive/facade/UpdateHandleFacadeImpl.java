package com.mfgroup.tgbot.service.receive.facade;

import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.service.receive.facade.additional.CallbackQueryHandlerFacade;
import com.mfgroup.tgbot.service.receive.facade.additional.MessageHandlerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class UpdateHandleFacadeImpl implements UpdateHandleFacade {
    @Autowired
    private CallbackQueryHandlerFacade cbQueryMainH;
    @Autowired
    private MessageHandlerFacade msgMainH;

    @Override
    public SendMsgEditMsgAdapter handleReceiveUpdate(Update update) {
        if (update.hasMessage()) {
            return receiveMsg(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return receiveCBQuery(update.getCallbackQuery());
        }
        return null;
    }

    private SendMsgEditMsgAdapter receiveMsg(Message message) {
        return msgMainH.shareReceiveObjToNeededHandler(message);
    }

    private SendMsgEditMsgAdapter receiveCBQuery(CallbackQuery callbackQuery) {
        return cbQueryMainH.shareReceiveObjToNeededHandler(callbackQuery);
    }
}
