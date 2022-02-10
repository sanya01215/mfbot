package com.mfgroup.tgBot.controller;

import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgBot.service.receive.facade.UpdateHandleFacade;
import com.mfgroup.tgBot.service.send.MessageSenderServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotMainControllerImpl implements BotMainController {
    @Autowired
    private UpdateHandleFacade updateHandleFacade;
    @Autowired
    private MessageSenderServ messageSenderServ;

    @Override
    public void processUpdate(Update update) {
        SendMsgEditMsgDecorator msgDecorator = updateHandleFacade.handleReceiveUpdate(update);
        if(msgDecorator !=null)sendAnswer(msgDecorator);
    }

    private void sendAnswer(SendMsgEditMsgDecorator msgDecorator) {
        messageSenderServ.sendAnswer(msgDecorator);
    }

}
