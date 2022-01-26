package com.MFGroup.MFTelegramBot.service.msghandler.impl;

import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.dao.BotData;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.Handler;
import com.MFGroup.MFTelegramBot.service.msgsender.MsgSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayList;
import java.util.List;

import static com.MFGroup.MFTelegramBot.dao.BotData.CallbackQueryHandlerSpeech.*;
import static com.MFGroup.MFTelegramBot.dao.BotData.UserPositionEnum.INPUT_QUIZ;

@Component
public class CallbackQueryHandlerService implements Handler<CallbackQuery> {

    private final KeyboardFactory keyboardFactory;
    private final MsgSender msgSender;
    private final Cache<User> cache;
    private final UserRepository userRepo;
    private final SendMsgEditMsgDecorator sendMessage;
    private final SendMsgEditMsgDecorator editMessageText;
    private List<String> quizAnswers;
    private User user;
    private Long lastMessageId;

    public CallbackQueryHandlerService(KeyboardFactory keyboardFactory, MsgSender msgSender, Cache<User> cache, UserRepository userRepo) {
        this.keyboardFactory = keyboardFactory;
        this.msgSender = msgSender;
        this.cache = cache;
        this.userRepo = userRepo;
        this.quizAnswers = new ArrayList<>();
        editMessageText = new EditMsgWrapper();
        sendMessage = new SendMsgWrapper();
    }

    @Override
    public void processReceivedObj(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        lastMessageId = chatId;
        String cbqData = callbackQuery.getData();
        user = cache.findById(chatId);
        if (user == null || user.getPosition() != INPUT_QUIZ) throw new RuntimeException("Received CallBack query from user who wasn't in the cache");

        switch (cbqData){
            case "OK":
                sendMessage(null, ALL_DONE, keyboardFactory.getMainReplyKeyBoardMarkup());
                userEndRegistration();
                break;
            case  "REMOVE":
                sendQuizEditMessage(null, YOUR_TAGS, null);
                break;
            default:
                putQuizAnswer(cbqData);
        }
    }

    private void putQuizAnswer(String cbqData) {
        if (!quizAnswers.isEmpty()) {
            sendQuizEditMessage(cbqData, YOUR_TAGS, keyboardFactory.getOkRemoveAttachKeyBoard());
        } else {
            sendMessage(cbqData, YOUR_TAGS, keyboardFactory.getOkRemoveAttachKeyBoard());
        }
    }

    private void sendQuizEditMessage(String cbqData, String text, InlineKeyboardMarkup inlineKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        else quizAnswers.remove(quizAnswers.size() - 1);
        msgSender.prepareAndSendMsgOrEditMsg(editMessageText, Math.toIntExact(lastMessageId),text + getUsersChosenTags(),inlineKeyboard);
    }

    private void sendMessage(String cbqData, String text, ReplyKeyboard replyKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        msgSender.prepareAndSendMsgOrEditMsg(sendMessage,Math.toIntExact(lastMessageId),text + getUsersChosenTags(), (InlineKeyboardMarkup) replyKeyboard);
    }

    private void userEndRegistration() {
        user.setPosition(BotData.UserPositionEnum.END_REGISTRATION);
        user.setQuizAnswers(quizAnswers);
        userRepo.save(user);
        quizAnswers = new ArrayList<>();
    }

    private String getUsersChosenTags() {
        return "\n" + String.join(TAGS_DELIMITER, quizAnswers);
    }
}