package com.MFGroup.MFTelegramBot.messagesender;

import com.MFGroup.MFTelegramBot.bot.MFBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public class MessageSenderImp implements MessageSender{
    public MFBot bot;

    @Override
    public void sendMessage(SendMessage sendMessage) {
        try {
            bot.execute(sendMessage);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
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
