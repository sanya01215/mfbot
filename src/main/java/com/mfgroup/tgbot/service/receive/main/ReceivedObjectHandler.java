package com.mfgroup.tgbot.service.receive.main;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;

public interface ReceivedObjectHandler<T> {
    SendMsgEditMsgDecorator shareReceiveObjToNeededHandler(T msg);
}
