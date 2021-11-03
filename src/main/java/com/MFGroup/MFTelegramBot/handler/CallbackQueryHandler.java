package com.MFGroup.MFTelegramBot.handler;

import com.MFGroup.MFTelegramBot.cache.Cache;
import com.MFGroup.MFTelegramBot.domain.Position;
import com.MFGroup.MFTelegramBot.domain.User;
import com.MFGroup.MFTelegramBot.handler.keyboard.KeyBoard;
import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
import com.MFGroup.MFTelegramBot.persistance.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private final MessageSender messageSender;

    private final Cache<User> cache;

    private final UserRepository userRepo;

    private SendMessage sendMessage;

    private final Set<String> quizAllQuestions;

    Set<String> quizAnswers ;
    public CallbackQueryHandler(MessageSender messageSender, Cache<User> cache, UserRepository userRepo) {
        quizAllQuestions = new LinkedHashSet<>();
        quizAllQuestions.add("eng");
        quizAllQuestions.add("ukr");
        quizAllQuestions.add("rus");
        quizAllQuestions.add("it");
        quizAllQuestions.add("psychology");
        quizAllQuestions.add("sport");
        quizAllQuestions.add("drawing");
        quizAllQuestions.add("nature");
        quizAllQuestions.add("business");
        quizAllQuestions.add("cryptocurrency");
        quizAllQuestions.add("travel");
        quizAllQuestions.add("law");
        this.messageSender = messageSender;
        this.cache = cache;
        this.userRepo = userRepo;
        quizAnswers= new LinkedHashSet<>();
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        User user = cache.findById(chatId);
        String cbqData = callbackQuery.getData();

        if (user != null && user.getPosition() == Position.INPUT_QUIZ) {
            if (quizAllQuestions.contains(cbqData)) {
                quizAnswers.add(cbqData);
            }
        }
        if(cbqData.equals("OK. Done it!") && user !=null)
        {
            sendMessage.setText("All done. Congratulations!!!");
            sendMessage.setReplyMarkup(KeyBoard.mainKbInit());
            user.setPosition(Position.END_REGISTRATION);
            user.setQuizAnswers(quizAnswers);
            userRepo.save(user);
            quizAnswers= new LinkedHashSet<>();
        }
        else{
            sendMessage.setText("One more");
        }
        messageSender.sendMessage(sendMessage);
    }
}