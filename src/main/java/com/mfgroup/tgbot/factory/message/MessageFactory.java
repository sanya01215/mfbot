package com.mfgroup.tgbot.factory.message;

import com.mfgroup.tgbot.cache.BotData;
import com.mfgroup.tgbot.model.message.adapter.impl.SendMsgAdapter;
import com.mfgroup.tgbot.factory.keyboard.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.HashMap;
import java.util.Map;

import static com.mfgroup.tgbot.cache.BotData.MessageHandlerSpeech.*;
import static com.mfgroup.tgbot.cache.BotData.UserPositionEnum.*;

/**
 * All factory methods returns SendMsgWrap object with no chatId or wrong chatId,
 * so I highly recommended to set valid chatId, before you sending the message,
 * to avoid NullPointerException
 */

@Component
public class MessageFactory {
    private KeyboardFactory keyboard;

    private final Map<BotData.UserPositionEnum, SendMsgAdapter> cachedMsgMap;

    public MessageFactory(@Autowired KeyboardFactory keyboard) {
        this.keyboard = keyboard;
        cachedMsgMap = new HashMap<>();
    }
    public MessageFactory(){
        this.keyboard = new KeyboardFactory();
        cachedMsgMap = new HashMap<>();
    }

    public SendMsgAdapter getStartMsg() {
        return cachedMsgMap.containsKey(START) ?
                getMsgFromCache(START) : putMsgInCacheAndGet(START, START_HELLO, keyboard.getOkCancelKB());
    }

    public SendMsgAdapter getAcceptMsg() {
        return cachedMsgMap.containsKey(ACCEPT) ?
                getMsgFromCache(ACCEPT) : putMsgInCacheAndGet(ACCEPT, TYPE_NAME);
    }

    public SendMsgAdapter getInputNameMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_FULLNAME) ?
                getMsgFromCache(INPUT_FULLNAME) : putMsgInCacheAndGet(INPUT_FULLNAME, replyText);
    }

    public SendMsgAdapter getInputAgeMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_AGE) ?
                getMsgFromCache(INPUT_AGE) : putMsgInCacheAndGet(INPUT_AGE, replyText);
    }

    public SendMsgAdapter getInputCityMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_CITY) ?
                getMsgFromCache(INPUT_CITY) : putMsgInCacheAndGet(INPUT_CITY, replyText, keyboard.getRegQuizInlineKeyBoard());
    }

    public SendMsgAdapter getDoneRegMsg(String replyText) {
        SendMsgAdapter resultMsg;
        if (cachedMsgMap.containsKey(DONE_REGISTRATION)) {
            resultMsg = getMsgFromCache(DONE_REGISTRATION);
            //because text of this msg will be changing every time we just set it every time it calls
            resultMsg.setText(replyText);
        }
        else resultMsg = putMsgInCacheAndGet(DONE_REGISTRATION, replyText, keyboard.getMainKBMarkup());
        return resultMsg;
    }

    private SendMsgAdapter getMsgFromCache(BotData.UserPositionEnum position) {
        return cachedMsgMap.get(position);
    }

    private SendMsgAdapter prepareMsg(String text, ReplyKeyboard replyKeyboard) {
        SendMsgAdapter sendMsg = new SendMsgAdapter();
        sendMsg.setText(text);
        sendMsg.setReplyMarkup(replyKeyboard);
        return sendMsg;
    }

    private SendMsgAdapter putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String replyText) {
        SendMsgAdapter newSendMsg = prepareMsg(replyText, keyboard.removeReplyKeyBoard());
        cachedMsgMap.put(userPos, newSendMsg);
        return newSendMsg;
    }

    private SendMsgAdapter putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String replyText, ReplyKeyboard replyKB) {
        SendMsgAdapter newSendMsg = prepareMsg(replyText, replyKB);
        cachedMsgMap.put(userPos, newSendMsg);
        return newSendMsg;
    }
}
