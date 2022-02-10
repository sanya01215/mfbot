package com.mfgroup.tgBot.service.receive.main;

import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;

public interface ReceivedObjectHandler<T> {
    SendMsgEditMsgDecorator shareReceiveObjToNeededHandler(T msg);
}
