package com.mfgroup.tgbot.service.receive.handler;

import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;

public interface Handler<T> {
    SendMsgEditMsgAdapter handleReceivedObj(T t, long userID);
}

