package com.MFGroup.MFTelegramBot.decorator.impl;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@NoArgsConstructor
public class SendMsgWrapper extends SendMessage implements SendMsgEditMsgDecorator {
    public SendMsgWrapper(String chatId, String text, ReplyKeyboard replyKeyboard) {
        super();
        this.setChatId(chatId);
        this.setText(text);
        this.setReplyMarkup(replyKeyboard);
    }

    @Override
    public void setChatId(int id) {
        super.setChatId(String.valueOf(id));
    }

    @Override
    public void setMessageId(Integer messageId) {
        super.setChatId(String.valueOf(messageId));
    }

}
