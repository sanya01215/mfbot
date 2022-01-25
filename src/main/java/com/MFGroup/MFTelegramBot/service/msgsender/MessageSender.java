package com.MFGroup.MFTelegramBot.service.msgsender;

import com.MFGroup.MFTelegramBot.decorator.SendMessageEditMessageDecorator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageSender {
    void prepareAndSendMsgOrEditMsg(SendMessageEditMessageDecorator msg, int msgId, String msgText, InlineKeyboardMarkup msgKeyBoard);
    void sendMessage(SendMessageEditMessageDecorator msg);
}
