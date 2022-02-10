package com.mfgroup.tgBot.service.receive.handler;

import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgBot.model.User;

public interface Handler<T> {
    SendMsgEditMsgDecorator handleReceivedObj(T t, User user);
}

