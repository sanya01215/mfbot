package com.MFGroup.MFTelegramBot.controller.impl;

import com.MFGroup.MFTelegramBot.controller.BotClient;
import com.MFGroup.MFTelegramBot.controller.BotClientDataSender;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class BotClientDataSenderImpl implements BotClientDataSender {
    private BotClient botClient;

    public BotClientDataSenderImpl(@Autowired BotClient botClient) {
        this.botClient = botClient;
    }

    @Override
    public Message sendMessage(SendMsgWrapper sendMessage) {
        Message message = null;
        try {
            message = botClient.execute((SendMessage)sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void sendEditMessage(EditMsgWrapper editMessageText) {
        try {
            botClient.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
