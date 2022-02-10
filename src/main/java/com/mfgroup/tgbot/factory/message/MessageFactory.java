package com.mfgroup.tgbot.factory.message;

import com.mfgroup.tgbot.cache.BotData;
import com.mfgroup.tgbot.model.decorator.impl.SendMsgWrap;
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

    private final Map<BotData.UserPositionEnum, SendMsgWrap> cachedMsgMap;

    public MessageFactory(@Autowired KeyboardFactory keyboard) {
        this.keyboard = keyboard;
        cachedMsgMap = new HashMap<>();
    }

    public SendMsgWrap getStartMsg() {
        return cachedMsgMap.containsKey(START) ?
                getMsgFromCache(START) : putMsgInCacheAndGet(START, START_HELLO, keyboard.getOkCancelKB());
    }

    public SendMsgWrap getAcceptMsg() {
        return cachedMsgMap.containsKey(ACCEPT) ?
                getMsgFromCache(ACCEPT) : putMsgInCacheAndGet(ACCEPT, TYPE_NAME);
    }

    public SendMsgWrap getInputNameMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_FULLNAME) ?
                getMsgFromCache(INPUT_FULLNAME) : putMsgInCacheAndGet(INPUT_FULLNAME, replyText);
    }

    public SendMsgWrap getInputAgeMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_AGE) ?
                getMsgFromCache(INPUT_AGE) : putMsgInCacheAndGet(INPUT_AGE, replyText);
    }

    public SendMsgWrap getInputCityMsg(String replyText) {
        return cachedMsgMap.containsKey(INPUT_CITY) ?
                getMsgFromCache(INPUT_CITY) : putMsgInCacheAndGet(INPUT_CITY, replyText, keyboard.getRegQuizInlineKeyBoard());
    }

    public SendMsgWrap getDoneRegMsg(String replyText) {
        SendMsgWrap resultMsg;
        if (cachedMsgMap.containsKey(DONE_REGISTRATION)) {
            resultMsg = getMsgFromCache(DONE_REGISTRATION);
            //because text of this msg will be changing every time we just set it every time it calls
            resultMsg.setText(replyText);
        }
        else resultMsg = putMsgInCacheAndGet(DONE_REGISTRATION, replyText, keyboard.getMainKBMarkup());
        return resultMsg;
    }

    private SendMsgWrap getMsgFromCache(BotData.UserPositionEnum position) {
        return cachedMsgMap.get(position);
    }

    private SendMsgWrap prepareMsg(String text, ReplyKeyboard replyKeyboard) {
        SendMsgWrap sendMsg = new SendMsgWrap();
        sendMsg.setText(text);
        sendMsg.setReplyMarkup(replyKeyboard);
        return sendMsg;
    }

    private SendMsgWrap putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String replyText) {
        SendMsgWrap newSendMsg = prepareMsg(replyText, keyboard.removeReplyKeyBoard());
        cachedMsgMap.put(userPos, newSendMsg);
        return newSendMsg;
    }

    private SendMsgWrap putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String replyText, ReplyKeyboard replyKB) {
        SendMsgWrap newSendMsg = prepareMsg(replyText, replyKB);
        cachedMsgMap.put(userPos, newSendMsg);
        return newSendMsg;
    }
}
