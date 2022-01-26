package com.MFGroup.MFTelegramBot.decorator.impl;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SendMsgWrapper extends SendMessage implements SendMsgEditMsgDecorator {
    @Override
    public void setChatId(int id) {
        super.setChatId(String.valueOf(id));
    }
}
