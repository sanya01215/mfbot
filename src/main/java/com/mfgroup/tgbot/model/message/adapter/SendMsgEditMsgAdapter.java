package com.mfgroup.tgbot.model.message.adapter;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface SendMsgEditMsgAdapter {
    void setText(String text);
    void setChatId(String id);
    void setReplyMarkup(ReplyKeyboard replyKeyboard);
    void setMessageId(Integer messageId);
    String  getText();

}
