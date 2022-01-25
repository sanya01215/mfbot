package com.MFGroup.MFTelegramBot.controller;

import com.MFGroup.MFTelegramBot.decorator.EditMessageWrapper;
import com.MFGroup.MFTelegramBot.decorator.SendMessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class BotClientDataSenderImpl implements BotClientDataSender{
    private BotClient botClient;

    public BotClientDataSenderImpl(@Autowired BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public Message sendMessage(SendMessageWrapper sendMessage) {
        Message message = null;
        try {
            message = botClient.execute((SendMessage)sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void sendEditMessage(EditMessageWrapper editMessageText) {
        try {
            botClient.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
