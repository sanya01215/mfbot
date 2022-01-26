package com.MFGroup.MFTelegramBot.decorator.impl;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
public class EditMsgWrapper extends EditMessageText implements SendMsgEditMsgDecorator {
    @Override
    public void setChatId(int id) {
        super.setMessageId(id);
    }

    @Override
    public void setReplyMarkup(ReplyKeyboard replyKeyboard) {
        super.setReplyMarkup((InlineKeyboardMarkup) replyKeyboard);
    }

}
