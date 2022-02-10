package com.mfgroup.tgBot.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotMainController {
    void processUpdate(Update update);
}
