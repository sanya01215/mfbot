package com.MFGroup.MFTelegramBot.service.msgsender.impl;

import com.MFGroup.MFTelegramBot.controller.BotClientDataSender;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.service.msgsender.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class MsgSenderImp implements MsgSender {
    //Fix circle dependencies caused by botclient class send and receive function
    @Lazy
    @Autowired
    private BotClientDataSender botClientDataSender;


    public void sendMessage(SendMsgEditMsgDecorator msg) {
        if (msg.getClass() == EditMsgWrapper.class) botClientDataSender.sendEditMessage((EditMsgWrapper) msg);
        if (msg.getClass() == SendMsgWrapper.class) botClientDataSender.sendMessage((SendMsgWrapper) msg);
    }

    @Override
    public void prepareAndSendMsgOrEditMsg(SendMsgEditMsgDecorator msg, int msgId, String msgText, InlineKeyboardMarkup msgKeyBoard) {
        msg.setChatId(msgId);
        if (msgText == null) throw new RuntimeException("Try to send message with empty text field");
        msg.setText(msgText);
        if (msgKeyBoard != null) msg.setReplyMarkup(msgKeyBoard);
        sendMessage(msg);
    }

}
