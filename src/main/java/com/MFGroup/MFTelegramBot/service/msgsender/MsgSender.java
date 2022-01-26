package com.MFGroup.MFTelegramBot.service.msgsender;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MsgSender {
    void prepareAndSendMsgOrEditMsg(SendMsgEditMsgDecorator msg, int msgId, String msgText, InlineKeyboardMarkup msgKeyBoard);
    void sendMessage(SendMsgEditMsgDecorator msg);
}
