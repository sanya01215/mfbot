package com.MFGroup.MFTelegramBot.handler;


import com.MFGroup.MFTelegramBot.cache.Cache;
import com.MFGroup.MFTelegramBot.domain.Position;
import com.MFGroup.MFTelegramBot.domain.User;
import com.MFGroup.MFTelegramBot.handler.keyboard.KeyBoard;
import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
import com.MFGroup.MFTelegramBot.persistance.UserRepository;
import com.MFGroup.MFTelegramBot.service.SearchUsers;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedHashSet;
import java.util.Set;


@Component
public class MessageHandler implements Handler<Message> {
    private final UserRepository userRepo;

    public final MessageSender messageSender;

    public final SearchUsers searchUsers;

    private SendMessage sendMessage;

    private final Cache<User> cache;

    private final Set<String> quizAnswers;

    public MessageHandler(UserRepository userRepo, MessageSender messageSender, SearchUsers searchUsers, Cache<User> cache) {
        quizAnswers = new LinkedHashSet<>();
        quizAnswers.add("eng");
        quizAnswers.add("ukr");
        quizAnswers.add("rus");
        quizAnswers.add("it");
        quizAnswers.add("psychology");
        quizAnswers.add("sport");
        quizAnswers.add("drawing");
        quizAnswers.add("nature");
        quizAnswers.add("business");
        quizAnswers.add("cryptocurrency");
        quizAnswers.add("travel");
        quizAnswers.add("law");
        this.userRepo = userRepo;
        this.messageSender = messageSender;
        this.searchUsers = searchUsers;
        this.cache = cache;
    }

    @Override
    public void choose(Message message) {
        String receivedMsg=message.getText().trim();
        String chatId=message.getChatId().toString();

        sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        User user = cache.findById(message.getChatId());

        //for user who are already in cache
        if (user != null) {

            switch (user.getPosition()) {
                case END_REGISTRATION:
                    if (receivedMsg.equals("Get all users")){
                    sendMessage.setText(userRepo.findAll().toString());
                    }
                    else if(receivedMsg.equals("Delete all users")){
                        userRepo.deleteAll();
                        sendMessage.setText(userRepo.findAll().toString());
                    }
                    else if(receivedMsg.equals("Find best match user")){
                        sendMessage.setText("Your best match user: " + searchUsers.findBestTagMatchUser(user.getQuizAnswers(),user.getChatId()));
                    }
                    else{
                        sendMessage.setText("Something wrong.(End REG P default text");
                        cache.removeById(message.getChatId());
                    }

                    break;
                case INPUT_FULLNAME:
                    if (message.hasText()) {
                        user.setName(message.getText());
                        user.setPosition(Position.INPUT_AGE);
                        sendMessage.setText("Type your age");
                    }
                    else {
                        sendMessage.setText("Name was incorrect. Please type again.");
                    }
                    break;

                case INPUT_AGE:
                        try {
                            user.setAge( Integer.parseInt(message.getText()));
                            user.setPosition(Position.INPUT_CITY);
                            sendMessage.setText("Type your City");
                        } catch (IllegalArgumentException e) {
                            sendMessage.setText("Age was incorrect. Please type again.");
                            e.printStackTrace();
                        }
                        break;


                case INPUT_CITY:
                    if (message.hasText()) {
                        user.setCity(message.getText());
                        sendMessage.setText("Now quiz. Tap on bottom which topic you prefer");
                        sendMessage.setReplyMarkup(KeyBoard.regQuizAttachKbInit(quizAnswers));
                        user.setPosition(Position.INPUT_QUIZ);
                    }
                    else {
                        sendMessage.setText("City was incorrect. Please type again.");
                    }
                    break;

                case INPUT_QUIZ:
                        user.setPosition(Position.NONE);
                        sendMessage.setText("QUIZ...");
                    break;

                default :
                    sendMessage.setText("Alright... Have a nice day!");
                    break;
            }

        }

        //for new users (start session)
        else {
            switch (receivedMsg) {
                case "/start":
                    sendMessage.setText("Hello! There is The Bot for meeting some new interesting people : \n" +
                            "Please answer for a few questions, that help us to find you suitable person. ");
                    sendMessage.setReplyMarkup(KeyBoard.continueCancelKbInit());
                    break;

                case "Ok. Let's go!":
                    sendMessage.setText("Type your Full Name");
                    User newUser = new User(message.getChat().getUserName(),message.getChatId());
                    newUser.setPosition(Position.INPUT_FULLNAME);
                    newUser.setChatId(message.getChatId());
                    cache.add(newUser);
                    break;

                default :
                    sendMessage.setText("Alright... Have a nice day!");
                    break;
            }
        }
        messageSender.sendMessage(sendMessage);
    }

}
