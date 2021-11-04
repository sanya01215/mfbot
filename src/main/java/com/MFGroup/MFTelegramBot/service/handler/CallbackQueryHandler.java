package com.MFGroup.MFTelegramBot.service.handler;

import com.MFGroup.MFTelegramBot.controller.MessageSender;
import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.model.RegistrationQuiz;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.model.UserPosition;
import com.MFGroup.MFTelegramBot.service.KeyBoardImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private final KeyBoardImpl keyBoard;

    private final MessageSender messageSender;

    private final Cache<User> cache;

    private final UserRepository userRepo;

    private SendMessage sendMessage;

    private Set<String> quizAnswers;

    private RegistrationQuiz registrationQuiz;

    private User user;

    public CallbackQueryHandler(KeyBoardImpl keyBoard, MessageSender messageSender, Cache<User> cache, UserRepository userRepo, SendMessage sendMessage, RegistrationQuiz registrationQuiz) {
        this.keyBoard = keyBoard;
        this.sendMessage = sendMessage;
        this.registrationQuiz = registrationQuiz;
        this.messageSender = messageSender;
        this.cache = cache;
        this.userRepo = userRepo;
        this.quizAnswers = new LinkedHashSet<>();
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String cbqData = callbackQuery.getData();

        sendMessage.setChatId(String.valueOf(chatId));
        user = cache.findById(chatId);
        if (user != null) {
            switch (user.getPosition()) {
                case INPUT_QUIZ:
                    if (registrationQuiz.getAllTags().contains(cbqData)) {
                        quizAnswers.add(cbqData);
                        messageSender.sendMessage(sendMessage);

                    } else if (cbqData.equals("OK. Done it!")) {
                        sendMessage.setText("All done. Congratulations!!!");
                        sendMessage.setReplyMarkup(keyBoard.mainKbInit());
                        user.setPosition(UserPosition.END_REGISTRATION);
                        user.setQuizAnswers(quizAnswers);
                        userRepo.save(user);
                        quizAnswers = new LinkedHashSet<>();
                    }
            }
        }

    }
}