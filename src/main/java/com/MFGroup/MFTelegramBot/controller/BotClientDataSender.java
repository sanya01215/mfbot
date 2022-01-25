package com.MFGroup.MFTelegramBot.controller;

import com.MFGroup.MFTelegramBot.decorator.EditMessageWrapper;
import com.MFGroup.MFTelegramBot.decorator.SendMessageWrapper;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotClientDataSender {
    Message sendMessage(SendMessageWrapper sendMessage);
    void sendEditMessage(EditMessageWrapper editMessageText);
}
