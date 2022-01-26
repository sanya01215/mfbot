package com.MFGroup.MFTelegramBot.controller.impl;

import com.MFGroup.MFTelegramBot.controller.BotClientDataReceiver;
import com.MFGroup.MFTelegramBot.dao.impl.CacheImpl;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.impl.CallbackQueryHandlerService;
import com.MFGroup.MFTelegramBot.service.msghandler.impl.MsgHandlerService;
import com.MFGroup.MFTelegramBot.service.msghandler.impl.RegMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotClientDataReceiverImpl implements BotClientDataReceiver {
    @Autowired
    private CallbackQueryHandlerService callbackQueryHandlerService;
    @Autowired
    private MsgHandlerService msgHandlerService;
    @Autowired
    private RegMsgHandler regMsgHandler;
    @Autowired
    private CacheImpl cache;

    private User user;

    public BotClientDataReceiverImpl() {
    }

    @Override
    public void executeMessage(Message message) {
        if(cache.findById(message.getChatId()) != null) regMsgHandler.processReceivedObj(message);
        else msgHandlerService.processReceivedObj(message);
    }

    @Override
    public void executeCallBackQuery(CallbackQuery callbackQuery) {
        callbackQueryHandlerService.processReceivedObj(callbackQuery);
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
