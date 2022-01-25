package com.MFGroup.MFTelegramBot.service.msghandler;

import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.dao.impl.BotData;
import com.MFGroup.MFTelegramBot.decorator.EditMessageWrapper;
import com.MFGroup.MFTelegramBot.decorator.SendMessageEditMessageDecorator;
import com.MFGroup.MFTelegramBot.decorator.SendMessageWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msgsender.MessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayList;
import java.util.List;

import static com.MFGroup.MFTelegramBot.dao.impl.BotData.UserPositionEnum.INPUT_QUIZ;

@Component
public class CallbackQueryHandlerService implements Handler<CallbackQuery> {

    private final KeyboardFactory keyBoard;
    private final MessageSender messageSender;
    private final Cache<User> cache;
    private final UserRepository userRepo;
    private final SendMessageEditMessageDecorator sendMessage;
    private final SendMessageEditMessageDecorator editMessageText;
    private List<String> quizAnswers;
    private User user;
    private Long lastMessageId;

    public CallbackQueryHandlerService(KeyboardFactory keyBoard, MessageSender messageSender, Cache<User> cache, UserRepository userRepo) {
        this.keyBoard = keyBoard;
        this.messageSender = messageSender;
        this.cache = cache;
        this.userRepo = userRepo;
        this.quizAnswers = new ArrayList<>();
        editMessageText = new EditMessageWrapper();
        sendMessage = new SendMessageWrapper();
    }

    @Override
    public void processReceivedObject(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        lastMessageId = chatId;
        String cbqData = callbackQuery.getData();
        user = cache.findById(chatId);
        if (user == null) return;
        if (cbqData.equals("OK")) {
            sendMessage(null, "All done. Congratulations!!!", keyBoard.getMainReplyKeyBoardMarkup());
            userEndRegistration();
        } else if (cbqData.equals("REMOVE")) {
            sendQuizEditMessage(null, "Your tags:", null);
        } else if (user.getPosition() == INPUT_QUIZ) {
            putQuizAnswer(cbqData);
        }
    }

    private void putQuizAnswer(String cbqData) {
        if (!quizAnswers.isEmpty()) {
            sendQuizEditMessage(cbqData, "Your tags:", keyBoard.getOkRemoveAttachKeyBoard());
        } else {
            sendMessage(cbqData, "Your tags:", keyBoard.getOkRemoveAttachKeyBoard());
        }
    }

    private void sendQuizEditMessage(String cbqData, String text, InlineKeyboardMarkup inlineKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        else quizAnswers.remove(quizAnswers.size() - 1);
        messageSender.prepareAndSendMsgOrEditMsg(editMessageText, Math.toIntExact(lastMessageId),text + getUsersChosenTags(),inlineKeyboard);
    }

    private void sendMessage(String cbqData, String text, ReplyKeyboard replyKeyboard) {
        if (cbqData != null && !quizAnswers.contains(cbqData)) quizAnswers.add(cbqData);
        sendMessage.setText(text + getUsersChosenTags());
        sendMessage.setReplyMarkup(replyKeyboard);
        messageSender.sendMessage(sendMessage);
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