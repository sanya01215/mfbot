package com.MFGroup.MFTelegramBot.controller;

import com.MFGroup.MFTelegramBot.dao.impl.CacheImpl;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.CallbackQueryHandlerService;
import com.MFGroup.MFTelegramBot.service.msghandler.MessageHandlerService;
import com.MFGroup.MFTelegramBot.service.msghandler.RegMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotClientProcessorImpl implements BotClientProcessor {
    @Autowired
    private CallbackQueryHandlerService callbackQueryHandlerService;
    @Autowired
    private MessageHandlerService messageHandlerService;
    @Autowired
    private RegMessageHandler regMessageHandler;
    @Autowired
    private CacheImpl cache;

    private User user;

    public BotClientProcessorImpl() {
    }

    @Override
    public void executeMessage(Message message) {
        if(cache.findById(message.getChatId()) != null)regMessageHandler.choose(message);
        else messageHandlerService.choose(message);
    }

    @Override
    public void executeCallBackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandlerService.choose(callbackQuery);
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
