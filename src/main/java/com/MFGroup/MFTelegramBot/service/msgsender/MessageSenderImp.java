package com.MFGroup.MFTelegramBot.service.msgsender;

import com.MFGroup.MFTelegramBot.controller.BotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Service
public class MessageSenderImp implements MessageSender {
    public BotClient bot;

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
    public void setBot(BotClient bot) {
        this.bot = bot;
    }

}
