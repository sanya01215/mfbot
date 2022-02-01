package com.MFGroup.MFTelegramBot.service.message.impl;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrap;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrap;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.message.Handler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

import static com.MFGroup.MFTelegramBot.cache.BotData.UserPositionEnum.*;
import static com.MFGroup.MFTelegramBot.cache.BotData.CallbackQueryHandlerSpeech.*;


@Component
public class CallbackQueryService implements Handler<CallbackQuery> {
    private final KeyboardFactory kbFactory;
    private final UserRepository userRepo;
    private Long lastMsgId;
    private Long chatId;
    private String chatIdStr;
    private final List<String> quizAnswers;

    public CallbackQueryService(KeyboardFactory kbFactory, UserRepository userRepo) {
        this.kbFactory = kbFactory;
        this.userRepo = userRepo;
        quizAnswers = new ArrayList<>();
    }

    @Override
    @Transactional
    public SendMsgEditMsgDecorator processReceivedObj(CallbackQuery callbackQuery) {
        chatId = callbackQuery.getMessage().getChatId();
        chatIdStr = String.valueOf(chatId);
        String cbqData = callbackQuery.getData();
        User user = userRepo.getById(chatId);
        lastMsgId = user.getLastMsgId();
        SendMsgEditMsgDecorator replyMsg;

        switch (cbqData) {
            case "OK":
                userEndRegistration(user, quizAnswers);
                replyMsg = new SendMsgWrap(chatIdStr,ALL_DONE, kbFactory.getMainKBMarkup());
                break;
            case "REMOVE":
                removeLastTagFromAnswers();
                replyMsg = new EditMsgWrap(chatIdStr,YOUR_TAGS + getChosenTags(quizAnswers), kbFactory.getOkRmAttachKB());
                replyMsg.setMessageId(Math.toIntExact(lastMsgId));
                break;
            default:
                replyMsg = putQuizAnswer(cbqData, quizAnswers);
        }
        return replyMsg;
    }

    private void removeLastTagFromAnswers() {
        quizAnswers.remove(quizAnswers.size() - 1);
    }

    private SendMsgEditMsgDecorator putQuizAnswer(String cbqData, List<String> answers) {
        if (!answers.isEmpty()) return putAnswerIfListNotEmpty(cbqData, answers);
        else return putAnswerIfListEmpty(cbqData, answers);
    }

    private SendMsgEditMsgDecorator putAnswerIfListNotEmpty(String cbqData, List<String> answers) {
        if (cbqData != null && !answers.contains(cbqData)) answers.add(cbqData);
        EditMsgWrap eMsgWrap = new EditMsgWrap(chatIdStr, YOUR_TAGS + getChosenTags(answers), kbFactory.getOkRmAttachKB());
        eMsgWrap.setMessageId(Math.toIntExact(lastMsgId));
        return eMsgWrap;
    }

    private SendMsgEditMsgDecorator putAnswerIfListEmpty(String cbqData, List<String> answers) {
        if (cbqData != null) answers.add(cbqData);
        return new SendMsgWrap(chatIdStr, YOUR_TAGS + getChosenTags(answers), kbFactory.getOkRmAttachKB());
    }

    private void userEndRegistration(User regUser, List<String> answers) {
        regUser.setPosition(FINISHED_REGISTRATION);
        regUser.setQuizAnswers(answers);
        userRepo.saveAndFlush(regUser);
    }

    private String getChosenTags(List<String> answers) {
        return "\n" + String.join(TAGS_DELIMITER, answers);
    }
}