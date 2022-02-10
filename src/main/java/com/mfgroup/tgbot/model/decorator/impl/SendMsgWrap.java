package com.mfgroup.tgbot.model.decorator.impl;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class SendMsgWrap extends SendMessage implements SendMsgEditMsgDecorator {
    public SendMsgWrap(String chatId, String text, ReplyKeyboard replyKeyboard) {
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

}
