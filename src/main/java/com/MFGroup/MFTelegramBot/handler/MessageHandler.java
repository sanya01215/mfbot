package com.MFGroup.MFTelegramBot.handler;


import com.MFGroup.MFTelegramBot.cache.Cache;
import com.MFGroup.MFTelegramBot.domain.Position;
import com.MFGroup.MFTelegramBot.domain.User;
import com.MFGroup.MFTelegramBot.handler.keyboard.KeyBoard;
import com.MFGroup.MFTelegramBot.messagesender.MessageSender;
import com.MFGroup.MFTelegramBot.persistance.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MessageHandler implements Handler<Message> {
    private final UserRepository userRepo;

    public final MessageSender messageSender;

    private SendMessage sendMessage;

    private String receivedMsg;

    private String chatId;

    private ReplyKeyboardMarkup replyKeyboardMarkup;

    private InlineKeyboardMarkup inlineKeyboardMarkup;

    private final Cache<User> cache;

    private final Map<String,Boolean> quizAnswers;
    {
        quizAnswers = new LinkedHashMap<>();
        quizAnswers.put("eng", false);
        quizAnswers.put("ukr", false);
        quizAnswers.put("rus", false);
        quizAnswers.put("it", false);
        quizAnswers.put("psychology", false);
        quizAnswers.put("sport", false);
        quizAnswers.put("drawing", false);
        quizAnswers.put("nature", false);
        quizAnswers.put("business", false);
        quizAnswers.put("cryptocurrency", false);
        quizAnswers.put("travel", false);
        quizAnswers.put("law", false);
    }


    public MessageHandler(UserRepository userRepo, MessageSender messageSender, Cache<User> cache) {
        this.userRepo = userRepo;
        this.messageSender = messageSender;
        this.cache = cache;
    }

    @Override
    public void choose(Message message) {
        receivedMsg = message.getText().trim();
        chatId = message.getChatId().toString();
        sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
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
//                    replyKeyboardMarkup = mainKbInit(replyKeyboardMarkup);
                    sendMessage.setText("Hello! There is The Bot for meeting some new interesting people : \n" +
                            "Please answer for a few questions, that help us to find you suitable person. ");
                    sendMessage.setReplyMarkup(KeyBoard.continueCancelKbInit());
                    break;

                case "Ok. Let's go!":
                    sendMessage.setText("Type your Full Name");
                    User newUser = new User();
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
