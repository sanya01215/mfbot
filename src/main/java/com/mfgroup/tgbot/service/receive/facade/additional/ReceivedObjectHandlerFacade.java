package com.mfgroup.tgbot.service.receive.facade.additional;

import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;

public interface ReceivedObjectHandlerFacade<T> {
    SendMsgEditMsgAdapter shareReceiveObjToNeededHandler(T msg);
}
