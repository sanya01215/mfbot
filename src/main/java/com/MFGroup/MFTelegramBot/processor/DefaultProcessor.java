package com.MFGroup.MFTelegramBot.processor;

import com.MFGroup.MFTelegramBot.handler.CallbackQueryHandler;
import com.MFGroup.MFTelegramBot.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
@Component
public class DefaultProcessor implements Processor{
    @Autowired
    public CallbackQueryHandler callbackQueryHandler;
    @Autowired
    public MessageHandler messageHandler;


    public DefaultProcessor() {
    }

    @Override
    public void executeMessage(Message message) {
        messageHandler.choose(message);
    }

    @Override
    public void executeCallBackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandler.choose(callbackQuery);
    }
}
