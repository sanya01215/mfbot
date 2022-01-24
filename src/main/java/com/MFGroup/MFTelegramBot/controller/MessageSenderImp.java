package com.MFGroup.MFTelegramBot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class MessageSenderImp implements MessageSender{
    public MFBot bot;

    @Override
    public Message sendMessage(SendMessage sendMessage) {
        Message message = null;
        try {
            message= bot.execute(sendMessage);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public void sendEditMessage(EditMessageText editMessageText) {
        try {
            bot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setBot(MFBot bot) {
        this.bot = bot;
    }

}
