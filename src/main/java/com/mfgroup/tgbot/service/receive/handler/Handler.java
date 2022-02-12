package com.mfgroup.tgbot.service.receive.handler;

import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.model.user.User;

public interface Handler<T> {
    SendMsgEditMsgAdapter handleReceivedObj(T t, User user);
}

