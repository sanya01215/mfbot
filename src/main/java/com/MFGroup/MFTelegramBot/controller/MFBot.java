package com.MFGroup.MFTelegramBot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class MFBot extends TelegramLongPollingBot {
    @Value("${bot.auth.username}")
    private String userName;
    @Value("${bot.auth.token}")
    private String token;
    private final Processor processor;

    public MFBot(Processor processor) {
        this.processor = processor;
    }

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}

