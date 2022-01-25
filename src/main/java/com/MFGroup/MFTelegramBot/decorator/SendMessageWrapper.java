package com.MFGroup.MFTelegramBot.decorator;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMessageWrapper extends SendMessage implements SendMessageEditMessageDecorator {
    @Override
    public void setChatId(int id) {
        super.setChatId(String.valueOf(id));
    }
}
