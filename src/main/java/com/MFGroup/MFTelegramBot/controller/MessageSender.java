package com.MFGroup.MFTelegramBot.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageSender {
    Message sendMessage(SendMessage sendMessage);
    void sendEditMessage(EditMessageText editMessageText);
}
