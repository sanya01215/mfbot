package com.mfgroup.tgbot.model.decorator.impl;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class EditMsgWrap extends EditMessageText implements SendMsgEditMsgDecorator {
    @Override
    public void setChatId(String id) {
        super.setChatId(id);
    }
    public EditMsgWrap(String chatId, String text, ReplyKeyboard replyMarkup) {
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
