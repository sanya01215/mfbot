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

import java.util.Calendar;
import java.util.Map;

@Component
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    private final MessageSender messageSender;

    private final Cache<User> cache;

    private final UserRepository userRepo;

    private SendMessage sendMessage;

    public CallbackQueryHandler(MessageSender messageSender, Cache<User> cache, UserRepository userRepo) {
        this.messageSender = messageSender;
        this.cache = cache;
        this.userRepo = userRepo;
    }

    @Override
    public void choose(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        User user = cache.findById(chatId);
        Map<String, Boolean> quizAnswers ;
        String cbqData = callbackQuery.getData();
        if (user != null && user.getPosition() == Position.INPUT_QUIZ) {
            quizAnswers = user.getQuizAnswers();
            if (quizAnswers.get(cbqData) != null) {
                quizAnswers.put(cbqData, true);
            }
        }
        if(cbqData.equals("OK. Done it!") && user !=null){
            sendMessage.setText("All done. Congratulations!!!");
            sendMessage.setReplyMarkup(KeyBoard.mainKbInit());
            user.setPosition(Position.END_REGISTRATION);
            user.setUsername(callbackQuery.getMessage().getChat().getUserName());
            user.setRegDate(Calendar.getInstance().getTime());
            userRepo.save(user);
        }
        else{
            sendMessage.setText("One more");
        }
        messageSender.sendMessage(sendMessage);
    }
}