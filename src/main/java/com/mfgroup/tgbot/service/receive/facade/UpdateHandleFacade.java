package com.mfgroup.tgbot.service.receive.facade;

import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandleFacade {
    SendMsgEditMsgAdapter handleReceiveUpdate(Update update);
}
