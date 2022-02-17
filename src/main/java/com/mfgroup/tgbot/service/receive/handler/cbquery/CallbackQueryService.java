package com.mfgroup.tgbot.service.receive.handler.cbquery;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.keyboard.KeyboardFactory;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.adapter.message.impl.EditMsgAdapter;
import com.mfgroup.tgbot.adapter.message.impl.SendMsgAdapter;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.service.receive.handler.Handler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.ArrayList;
import java.util.List;

import static com.mfgroup.tgbot.botdata.BotData.CallbackQueryHandlerSpeech.*;
import static com.mfgroup.tgbot.botdata.BotData.UserPositionEnum.DONE_REGISTRATION;


@Service
public class CallbackQueryService implements Handler<CallbackQuery> {
    private final KeyboardFactory kbFactory;
    private final UserRepository userRepo;

    public CallbackQueryService(KeyboardFactory kbFactory, UserRepository userRepo) {
        this.kbFactory = kbFactory;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public SendMsgEditMsgAdapter handleReceivedObj(CallbackQuery callbackQuery, long userId) {
        User user = userRepo.getById(userId);
        List<String> quizAnswers = user.getQuizAnswers();
        if(quizAnswers == null)quizAnswers = new ArrayList<>();
        Long chatId = callbackQuery.getMessage().getChatId();
        String chatIdStr = String.valueOf(chatId);
        String cbqData = callbackQuery.getData();
        Long lastMsgId = user.getLastMsgId();
        SendMsgEditMsgAdapter replyMsg;

        switch (cbqData) {
            case "OK":
                userEndRegistration(user, quizAnswers);
                replyMsg = new SendMsgAdapter(chatIdStr, ALL_DONE, kbFactory.getMainKBMarkup());
                break;
            case "REMOVE":
                removeLastTagFromAnswers(quizAnswers);
                replyMsg = new EditMsgAdapter(chatIdStr, YOUR_TAGS + getChosenTags(quizAnswers), kbFactory.getOkRmAttachKB());
                replyMsg.setMessageId(Math.toIntExact(lastMsgId));
                break;
            default:
                replyMsg = putQuizAnswer(cbqData, quizAnswers, lastMsgId, chatIdStr);
        }
        replyMsg.setChatId(chatIdStr);
        saveAnswersIntoRepo(userRepo,quizAnswers,user);

        return replyMsg;
    }

    private void removeLastTagFromAnswers(List<String> quizAnswers) {
        quizAnswers.remove(quizAnswers.size() - 1);
    }

    private SendMsgEditMsgAdapter putQuizAnswer(String cbqData, List<String> answers, Long lastMsgId, String chatIdStr) {
        if (!answers.isEmpty()) return putAnswerIfListNotEmpty(cbqData, answers, lastMsgId, chatIdStr);
        else return putAnswerIfListEmpty(cbqData, answers, chatIdStr);
    }

    private SendMsgEditMsgAdapter putAnswerIfListNotEmpty(String cbqData, List<String> answers, Long lastMsgId, String chatIdStr) {
        if (cbqData != null && !answers.contains(cbqData)) answers.add(cbqData);
        EditMsgAdapter eMsgWrap = new EditMsgAdapter(chatIdStr, YOUR_TAGS + getChosenTags(answers), kbFactory.getOkRmAttachKB());
        eMsgWrap.setMessageId(Math.toIntExact(lastMsgId));
        return eMsgWrap;
    }

    private SendMsgEditMsgAdapter putAnswerIfListEmpty(String cbqData, List<String> answers, String chatIdStr) {
        if (cbqData != null) answers.add(cbqData);
        return new SendMsgAdapter(chatIdStr, YOUR_TAGS + getChosenTags(answers), kbFactory.getOkRmAttachKB());
    }

    private void userEndRegistration(User regUser, List<String> answers) {
        regUser.setPosition(DONE_REGISTRATION);
        regUser.setQuizAnswers(answers);
        userRepo.saveAndFlush(regUser);
    }

    private String getChosenTags(List<String> answers) {
        return "\n" + String.join(TAGS_DELIMITER, answers);
    }

    private void saveAnswersIntoRepo(UserRepository userRepo,List<String> answers ,User user){
        user.setQuizAnswers(answers);
    }
}