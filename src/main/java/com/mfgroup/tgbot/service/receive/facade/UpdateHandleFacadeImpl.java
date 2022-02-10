package com.mfgroup.tgbot.service.receive.facade;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgbot.service.receive.main.CallbackQueryMainHandler;
import com.mfgroup.tgbot.service.receive.main.MessageMainHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
@Service
public class UpdateHandleFacadeImpl implements UpdateHandleFacade {
    @Autowired
    private  CallbackQueryMainHandler cbQueryMainH;
    @Autowired
    private MessageMainHandler msgMainH;

    @Override
    public SendMsgEditMsgDecorator handleReceiveUpdate(Update update) {
        if (update.hasMessage()) {
            return receiveMsg(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return receiveCBQuery(update.getCallbackQuery());
        }
        return null;
    }

    private SendMsgEditMsgDecorator receiveMsg(Message message) {
        return msgMainH.shareReceiveObjToNeededHandler(message);
    }

    private SendMsgEditMsgDecorator receiveCBQuery(CallbackQuery callbackQuery) {
        return cbQueryMainH.shareReceiveObjToNeededHandler(callbackQuery);
    }
}
