package com.mfgroup.tgbot.service.receive.facade;

import com.mfgroup.tgbot.model.decorator.SendMsgEditMsgDecorator;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandleFacade {
    SendMsgEditMsgDecorator handleReceiveUpdate(Update update);
}
