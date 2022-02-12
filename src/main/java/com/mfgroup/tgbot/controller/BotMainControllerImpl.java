package com.mfgroup.tgbot.controller;

import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.service.receive.facade.UpdateHandleFacade;
import com.mfgroup.tgbot.service.send.MessageSenderServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotMainControllerImpl implements BotMainController {
    @Autowired
    private UpdateHandleFacade updateHandleFacade;
    @Autowired
    //use lazy for fix circle dependencies error
    @Lazy
    private MessageSenderServ messageSenderServ;

    @Override
    public void processUpdate(Update update) {
        SendMsgEditMsgAdapter msgDecorator = updateHandleFacade.handleReceiveUpdate(update);
        if(msgDecorator !=null)sendAnswer(msgDecorator);
    }

    private void sendAnswer(SendMsgEditMsgAdapter msgDecorator) {
        messageSenderServ.sendAnswer(msgDecorator);
    }

}
