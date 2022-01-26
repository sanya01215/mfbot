package com.MFGroup.MFTelegramBot.service.msghandler;

public interface Handler<T> {
    void processReceivedObj(T t);
}

