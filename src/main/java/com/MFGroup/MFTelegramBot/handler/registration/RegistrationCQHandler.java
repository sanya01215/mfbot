//package com.MFGroup.MFTelegramBot.handler.registration;
//
//import com.MFGroup.MFTelegramBot.handler.Handler;
//import com.MFGroup.MFTelegramBot.handler.registration.quiz.EntryQuiz;
//import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
//import com.MFGroup.MFTelegramBot.domain.User;
//import com.MFGroup.MFTelegramBot.persistance.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
//
//import java.util.Calendar;
//import java.util.Map;
//@Component
//public class RegistrationCQHandler implements Handler<CallbackQuery> {
//    @Autowired
//    private UserRepository accountRepo;
//
//    public final MessageSender messageSender;
//
//    private SendMessage sendMessage;
//
//    private ReplyKeyboardMarkup replyKeyboardMarkup;
//
//    private InlineKeyboardMarkup inlineKeyboardMarkup;
//
//    private EntryQuiz entryQuiz;
//
//    private User user;
//
//    public RegistrationCQHandler(MessageSender messageSender) {
//        this.messageSender = messageSender;
//    }
//
//    @Override
//    public void choose(CallbackQuery callbackQuery) {
//        String cqData = callbackQuery.getData().toString();
//        entryQuiz = new EntryQuiz();
//        entryQuiz.setQuizTime(Calendar.getInstance().getTime());
//
//        for(Map.Entry entry: entryQuiz.getQuizAnswers().entrySet()){
//            if(entry.getKey().toString().equals(cqData))entryQuiz.getQuizAnswers().put(entry.getKey().toString(),true);
//            if(entry.getKey().toString().equals("OK. Done it!")){
//                saveUser(entryQuiz,callbackQuery);
//            }
//        }
//
//
//    }
//    private void saveUser(EntryQuiz entryQuiz, CallbackQuery callbackQuery){
//        user = new User(callbackQuery.getMessage().getChat().getUserName(),
//                callbackQuery.getMessage().getChatId(),callbackQuery.getMessage().getChat().getFirstName());
//        accountRepo.save(user);
//    }
//}
