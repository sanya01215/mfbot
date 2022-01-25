package com.MFGroup.MFTelegramBot.service.msgsender;

import com.MFGroup.MFTelegramBot.controller.BotClientDataSender;
import com.MFGroup.MFTelegramBot.decorator.EditMessageWrapper;
import com.MFGroup.MFTelegramBot.decorator.SendMessageEditMessageDecorator;
import com.MFGroup.MFTelegramBot.decorator.SendMessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class MessageSenderImp implements MessageSender {
    //Fix circle dependencies
    @Autowired
    @Lazy
    private BotClientDataSender botClientDataSender;



    public void sendMessage(SendMessageEditMessageDecorator msg) {
        if (msg.getClass() == EditMessageWrapper.class) botClientDataSender.sendEditMessage((EditMessageWrapper) msg);
        if (msg.getClass() == SendMessageWrapper.class) botClientDataSender.sendMessage((SendMessageWrapper) msg);
    }

    @Override
    public void prepareAndSendMsgOrEditMsg(SendMessageEditMessageDecorator msg, int msgId, String msgText, InlineKeyboardMarkup msgKeyBoard) {
        msg.setChatId(msgId);
        if (msgText != null) msg.setText(msgText);
        if (msgKeyBoard != null) msg.setReplyMarkup(msgKeyBoard);
        sendMessage(msg);
    }

}
