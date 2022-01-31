package com.MFGroup.MFTelegramBot.service.message;

import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;

public interface Handler<T> {
    SendMsgEditMsgDecorator processReceivedObj(T t);
}

