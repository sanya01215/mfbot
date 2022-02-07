package com.MFGroup.MFTelegramBot.service.bot;

import com.MFGroup.MFTelegramBot.controller.impl.BotClientControllerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class BotClient extends TelegramLongPollingBot {
    @Value("${bot.auth.username}")
    private String userName;
    @Value("${bot.auth.token}")
    private String token;
    private final BotClientControllerImpl botClientControllerImpl;

    public BotClient(BotClientControllerImpl botClientControllerImpl) {
        this.botClientControllerImpl = botClientControllerImpl;
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
        botClientControllerImpl.process(update);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}

