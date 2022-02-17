package com.mfgroup.tgbot.adapter.message.impl;

import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class EditMsgAdapter extends EditMessageText implements SendMsgEditMsgAdapter {
    @Override
    public void setChatId(String id) {
        super.setChatId(id);
    }
    public EditMsgAdapter(String chatId, String text, ReplyKeyboard replyMarkup) {
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

    @Override
    public @NonNull String getText() {
        return super.getText();
    }
}
