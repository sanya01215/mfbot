package com.MFGroup.MFTelegramBot.decorator.impl;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class EditMsgWrapper extends EditMessageText implements SendMsgEditMsgDecorator {
    @Override
    public void setChatId(int id) {
        super.setMessageId(id);
    }
    public EditMsgWrapper(String chatId, String text, ReplyKeyboard replyMarkup) {
        super();
        this.setChatId(chatId);
        this.setText(text);
        this.setReplyMarkup(replyMarkup);
    }
    @Override
    public void setReplyMarkup(ReplyKeyboard replyKeyboard) {
        super.setReplyMarkup((InlineKeyboardMarkup) replyKeyboard);
    }

    @Override
    public void setMessageId(Integer messageId) {
        super.setMessageId(messageId);
    }
}
