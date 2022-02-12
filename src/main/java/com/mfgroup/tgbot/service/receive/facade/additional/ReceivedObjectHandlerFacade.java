package com.mfgroup.tgbot.service.receive.facade.additional;

import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;

public interface ReceivedObjectHandlerFacade<T> {
    SendMsgEditMsgAdapter shareReceiveObjToNeededHandler(T msg);
}
