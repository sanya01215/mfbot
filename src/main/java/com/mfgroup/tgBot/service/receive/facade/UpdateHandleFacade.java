package com.mfgroup.tgBot.service.receive.facade;

import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandleFacade {
    SendMsgEditMsgDecorator handleReceiveUpdate(Update update);
}
