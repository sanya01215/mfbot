package com.MFGroup.MFTelegramBot.service.msghandler;

public interface Handler<T> {
    void choose(T t);
}
