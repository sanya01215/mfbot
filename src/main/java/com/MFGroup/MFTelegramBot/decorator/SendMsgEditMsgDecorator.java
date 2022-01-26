package com.MFGroup.MFTelegramBot.decorator;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface SendMsgEditMsgDecorator {
    void setText(String text);
    void setChatId(int id);
    void setReplyMarkup(ReplyKeyboard replyKeyboard);

}
