package com.mfgroup.tgbot.model.message.adapter.impl;

import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class SendMsgAdapter extends SendMessage implements SendMsgEditMsgAdapter {
    public SendMsgAdapter(String chatId, String text, ReplyKeyboard replyKeyboard) {
        super();
        this.setChatId(chatId);
        this.setText(text);
        this.setReplyMarkup(replyKeyboard);
    }

    @Override
    public void setChatId(String id) {
        super.setChatId(id);
    }

    @Override
    public void setMessageId(Integer messageId) {
        throw new RuntimeException("Unsupported operation");
    }

    @Override
    public @NonNull String getText() {
        return super.getText();
    }
}
