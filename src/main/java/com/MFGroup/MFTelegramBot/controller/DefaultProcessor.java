package com.MFGroup.MFTelegramBot.controller;

import com.MFGroup.MFTelegramBot.dao.CacheImpl;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.handler.CallbackQueryHandler;
import com.MFGroup.MFTelegramBot.service.handler.MessageHandler;
import com.MFGroup.MFTelegramBot.service.handler.RegMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DefaultProcessor implements Processor {
    @Autowired
    private   CallbackQueryHandler callbackQueryHandler;
    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private RegMessageHandler regMessageHandler;
    @Autowired
    private CacheImpl cache;

    private User user;

    public DefaultProcessor() {
    }

    @Override
    public void executeMessage(Message message) {
        if(cache.findById(message.getChatId()) != null)regMessageHandler.choose(message);
        else messageHandler.choose(message);
    }

    @Override
    public void executeCallBackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandler.choose(callbackQuery);
    }

    @Override
    public void process(Update update) {
        if(update.hasMessage()){
            executeMessage(update.getMessage());
        }
        else if(update.hasCallbackQuery()){
            executeCallBackQuery(update.getCallbackQuery());
        }
    }
}
