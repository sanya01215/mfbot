package com.MFGroup.MFTelegramBot.factory.message;

import com.MFGroup.MFTelegramBot.cache.BotData;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrap;
import com.MFGroup.MFTelegramBot.factory.keyboard.KeyboardFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.HashMap;
import java.util.Map;

import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.*;
import static com.MFGroup.MFTelegramBot.cache.BotData.UserPositionEnum.*;

@Component
public class MessageFactory {
    private KeyboardFactory keyboard;

    private final Map<BotData.UserPositionEnum, SendMsgWrap> cachedMsgMap;

    public MessageFactory(@Autowired KeyboardFactory keyboard) {
        this.keyboard = keyboard;
        cachedMsgMap = new HashMap<>();
    }

    public SendMsgWrap getStartMsg(String chatId) {
        return cachedMsgMap.containsKey(START) ?
                getMsgFromCache(START, chatId) : putMsgInCacheAndGet(START, chatId, START_HELLO, keyboard.getOkCancelKB());
    }

    public SendMsgWrap getAcceptMsg(String chatId) {
        return cachedMsgMap.containsKey(ACCEPT) ?
                getMsgFromCache(ACCEPT, chatId) : putMsgInCacheAndGet(ACCEPT, chatId, TYPE_NAME);
    }

    public SendMsgWrap getInputNameMsg(String chatId, String replyText) {
        return cachedMsgMap.containsKey(INPUT_FULLNAME) ?
                getMsgFromCache(INPUT_FULLNAME, chatId) : putMsgInCacheAndGet(INPUT_FULLNAME, chatId, replyText);
    }

    public SendMsgWrap getInputAgeMsg(String chatId, String replyText) {
        return cachedMsgMap.containsKey(INPUT_AGE) ?
                getMsgFromCache(INPUT_AGE, chatId) : putMsgInCacheAndGet(INPUT_AGE, chatId, replyText);
    }

    public SendMsgWrap getInputCityMsg(String chatId, String replyText) {
        return cachedMsgMap.containsKey(INPUT_CITY) ?
                getMsgFromCache(INPUT_CITY, chatId) : putMsgInCacheAndGet(INPUT_CITY, chatId, replyText, keyboard.getRegQuizInlineKeyBoard());
    }

    public SendMsgWrap getDoneRegMsg(String chatId, String replyText) {
        return cachedMsgMap.containsKey(DONE_REGISTRATION) ?
                getMsgFromCache(DONE_REGISTRATION, chatId) : putMsgInCacheAndGet(DONE_REGISTRATION, chatId, replyText, keyboard.getMainKBMarkup());
    }

    private SendMsgWrap getMsgFromCache(BotData.UserPositionEnum position, String chatId) {
        SendMsgWrap cachedMsg = cachedMsgMap.get(position);
        cachedMsg.setChatId(chatId);
        return cachedMsg;
    }

    private SendMsgWrap prepareMsg(String chatId, String text, ReplyKeyboard replyKeyboard) {
        return new SendMsgWrap(chatId, text, replyKeyboard);
    }

    private SendMsgWrap putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String chatId, String replyText) {
        return cachedMsgMap.put(userPos, prepareMsg(chatId, replyText, keyboard.removeReplyKeyBoard()));
    }

    private SendMsgWrap putMsgInCacheAndGet(BotData.UserPositionEnum userPos, String chatId, String replyText, ReplyKeyboard replyKB) {
        return cachedMsgMap.put(userPos, prepareMsg(chatId, replyText, replyKB));
    }
}
