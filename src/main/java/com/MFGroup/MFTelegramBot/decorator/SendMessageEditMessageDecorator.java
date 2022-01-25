package com.MFGroup.MFTelegramBot.decorator;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface SendMessageEditMessageDecorator {
    void setText(String text);
    void setChatId(int id);
    void setReplyMarkup(ReplyKeyboard replyKeyboard);

}
