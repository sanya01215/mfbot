package com.MFGroup.MFTelegramBot.service.msghandler;

import com.MFGroup.MFTelegramBot.dao.impl.BotData;
import com.MFGroup.MFTelegramBot.service.msgsender.MessageSender;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.dao.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.*;


import java.util.ArrayList;
import java.util.List;

import static com.MFGroup.MFTelegramBot.dao.impl.BotData.UserPositionEnum.INPUT_QUIZ;

@Component
public class CallbackQueryHandlerService implements Handler<CallbackQuery> {

    private final KeyboardFactory keyBoard;
    private final MessageSender messageSender;
    private final Cache<User> cache;
    private final UserRepository userRepo;
    private final SendMessage sendMessage;
    private final EditMessageText editMessageText;
    private List<String> quizAnswers;
    private User user;
    private Integer lastMessageId;

    public CallbackQueryHandlerService(KeyboardFactory keyBoard, MessageSender messageSender, Cache<User> cache, UserRepository userRepo) {
        this.editMessageText = new EditMessageText();
        this.sendMessage = new SendMessage();
        this.keyBoard = keyBoard;
        this.messageSender = messageSender;
        this.cache = cache;
        this.userRepo = userRepo;
        this.quizAnswers = new ArrayList<>();
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String cbqData = callbackQuery.getData();
        sendMessage.setChatId(String.valueOf(chatId));
        editMessageText.setChatId(String.valueOf(chatId));
        user = cache.findById(chatId);
        if (user == null) return;
        if (cbqData.equals("OK")) {
            sendMessage(null, "All done. Congratulations!!!", keyBoard.getMainReplyKeyBoardMarkup());
            userEndRegistration();
        } else if (cbqData.equals("REMOVE")) {
            sendEditMessage(null, "Your tags:", null);
        } else if (user.getPosition() == INPUT_QUIZ) {
            putQuizAnswer(cbqData);
        }
    }

    private void putQuizAnswer(String cbqData) {
        if (!quizAnswers.isEmpty()) {
            sendEditMessage(cbqData, "Your tags:", keyBoard.getOkRemoveAttachKeyBoard());
        } else {
            sendMessage(cbqData, "Your tags:", keyBoard.getOkRemoveAttachKeyBoard());
        }
    }

    private void sendEditMessage(String cbqData, String text, InlineKeyboardMarkup inlineKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        else quizAnswers.remove(quizAnswers.size() - 1);
        editMessageText.setText(text + getUsersChosenTags());
        editMessageText.setMessageId(lastMessageId);
        if (inlineKeyboard != null) editMessageText.setReplyMarkup(inlineKeyboard);
        messageSender.sendEditMessage(editMessageText);
        lastMessageId = editMessageText.getMessageId();
    }

    private void sendMessage(String cbqData, String text, ReplyKeyboard replyKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        sendMessage.setText(text + getUsersChosenTags());
        sendMessage.setReplyMarkup(replyKeyboard);
        Message sentMessage = messageSender.sendMessage(sendMessage);
        lastMessageId = sentMessage.getMessageId();
    }

    private void userEndRegistration() {
        user.setPosition(BotData.UserPositionEnum.END_REGISTRATION);
        user.setQuizAnswers(quizAnswers);
        userRepo.save(user);
        quizAnswers = new ArrayList<>();
    }

    private String getUsersChosenTags() {
        return "\n" + String.join(" || ", quizAnswers);
    }
}