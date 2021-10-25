package com.MFGroup.MFTelegramBot.service;

import com.MFGroup.MFTelegramBot.bot.MFBot;
import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService {
    private final MessageSender messageSender;
    private MFBot bot;

    @Autowired
    public void setBot(MFBot bot) {
        this.bot = bot;
    }

    public SendMessageService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

}
