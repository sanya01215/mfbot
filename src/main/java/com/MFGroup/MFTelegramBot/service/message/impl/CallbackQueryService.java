package com.MFGroup.MFTelegramBot.service.message.impl;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
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
    private final KeyboardFactory keyboardFactory;
    private final UserRepository userRepo;
    private Long lastMessageId;
    private Long chatId;
    private String chatIdStr;
    private final List<String> quizAnswers;
    public CallbackQueryService(KeyboardFactory keyboardFactory, UserRepository userRepo) {
        this.keyboardFactory = keyboardFactory;
        this.userRepo = userRepo;
        quizAnswers = new ArrayList<>();
    }

    @Override
    @Transactional
    public SendMsgEditMsgDecorator processReceivedObj(CallbackQuery callbackQuery) {
        chatId = callbackQuery.getMessage().getChatId();
        chatIdStr = String.valueOf(chatId);
        String cbqData = callbackQuery.getData();
        User user  = userRepo.getById(chatId);
        lastMessageId = user.getLastMsgId();
        SendMsgEditMsgDecorator replyMsg;

        switch (cbqData){
            case "OK":
                replyMsg =new SendMsgWrapper(chatIdStr, ALL_DONE, keyboardFactory.getMainKBMarkup());
                userEndRegistration(user,quizAnswers);
                break;
            case "REMOVE":
                quizAnswers.remove(quizAnswers.size() - 1);
                replyMsg = new EditMsgWrapper(chatIdStr,YOUR_TAGS+ getChosenTags(quizAnswers), keyboardFactory.getOkRemoveAttachKB());
                replyMsg.setMessageId(Math.toIntExact(lastMessageId));
                break;
            default:
                replyMsg = putQuizAnswer(cbqData,quizAnswers);
        }
        return replyMsg;
    }

    private SendMsgEditMsgDecorator putQuizAnswer(String cbqData,List<String> answers) {
        if (!answers.isEmpty()) return putAnswerIfListNotEmpty(cbqData,answers);
        else return putAnswerIfListEmpty(cbqData, answers);
    }
    private SendMsgEditMsgDecorator putAnswerIfListNotEmpty(String cbqData, List<String> answers){
        if (cbqData != null && !answers.contains(cbqData)) answers.add(cbqData);
        EditMsgWrapper eMsgWrap = new EditMsgWrapper (chatIdStr,YOUR_TAGS+ getChosenTags(answers), keyboardFactory.getOkRemoveAttachKB());
        eMsgWrap.setMessageId(Math.toIntExact(lastMessageId));
        return eMsgWrap;
    }
    private SendMsgEditMsgDecorator putAnswerIfListEmpty(String cbqData, List<String> answers){
        if (cbqData != null) answers.add(cbqData);
        return new SendMsgWrapper(chatIdStr,YOUR_TAGS+ getChosenTags(answers), keyboardFactory.getOkRemoveAttachKB());
    }

    private void userEndRegistration(User regUser,List<String> answers ) {
        regUser.setPosition(FINISHED_REGISTRATION);
        regUser.setQuizAnswers(answers);
        userRepo.saveAndFlush(regUser);
    }

    private String getChosenTags(List<String> answers) {
        return "\n" + String.join(TAGS_DELIMITER, answers);
    }
}