package com.mfgroup.tgBot.model.decorator;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface SendMsgEditMsgDecorator {
    void setText(String text);
    void setChatId(String id);
    void setReplyMarkup(ReplyKeyboard replyKeyboard);
    public void setMessageId(Integer messageId);

}
