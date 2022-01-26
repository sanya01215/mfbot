package com.MFGroup.MFTelegramBot.controller;

import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotClientDataSender {
    Message sendMessage(SendMsgWrapper sendMessage);
    void sendEditMessage(EditMsgWrapper editMessageText);
}
