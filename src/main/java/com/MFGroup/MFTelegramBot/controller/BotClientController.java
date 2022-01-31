package com.MFGroup.MFTelegramBot.controller;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotClientController {
    void receiveMsg(Message message);
    void receiveCBQuery(CallbackQuery callbackQuery);
    void process(Update update);
}
