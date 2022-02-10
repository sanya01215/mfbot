package com.mfgroup.tgbot.service.receive.handler;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgbot.model.User;

public interface Handler<T> {
    SendMsgEditMsgDecorator handleReceivedObj(T t, User user);
}

