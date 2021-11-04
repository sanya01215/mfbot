package com.MFGroup.MFTelegramBot.service.handler;

import com.MFGroup.MFTelegramBot.controller.MessageSender;
import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.model.RegistrationQuiz;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.model.UserPosition;
import com.MFGroup.MFTelegramBot.service.KeyBoardImpl;
import com.MFGroup.MFTelegramBot.service.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class RegMessageHandler implements Handler<Message>{
    private final KeyBoardImpl keyBoard;

    private final UserRepository userRepo;

    public final MessageSender messageSender;

    public final UserSearch userSearch;

    private SendMessage sendMessage;

    private final Cache<User> cache;

    private final RegistrationQuiz registrationQuiz;

    private final RegMessageHandler regMessageHandler;

    private User user;

    public RegMessageHandler(KeyBoardImpl keyBoard, UserRepository userRepo, MessageSender messageSender, UserSearch userSearch, SendMessage sendMessage, Cache<User> cache, RegistrationQuiz registrationQuiz, RegMessageHandler regMessageHandler) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.messageSender = messageSender;
        this.userSearch = userSearch;
        this.sendMessage = sendMessage;
        this.cache = cache;
        this.registrationQuiz = registrationQuiz;
        this.regMessageHandler = regMessageHandler;
    }

    @Override
    public void choose(Message message) {
        String chatId=message.getChatId().toString();
        sendMessage.setChatId(chatId);
        user = cache.findById(message.getChatId());

        switch (user.getPosition()) {
            case END_REGISTRATION:
                sendMessage.setText(endRegistration(message));
                break;

            case INPUT_FULLNAME:
                sendMessage.setText(inputFullName(message));
                break;

            case INPUT_AGE:
               sendMessage.setText(inputAge(message));
                break;

            case INPUT_CITY:
                sendMessage.setText(inputCity(message));
                sendMessage.setReplyMarkup(keyBoard.regQuizAttachKbInit(registrationQuiz.getAllTags()));
                user.setPosition(UserPosition.INPUT_QUIZ);
                break;

            default :
                sendMessage.setText("Alright... Something wrong!");
                break;
        }
    }
    private String endRegistration(Message message){
        String msg = message.getText();
        String answer;
        if (msg.equals("Get all users")){
            answer = userRepo.findAll().toString();
        }
        else if(msg.equals("Delete all users")){
            userRepo.deleteAll();
            answer= userRepo.findAll().toString();
        }
        else if(msg.equals("Find best match user")){
            answer= "Your best match user: " + userSearch.findBestTagMatchUser(user.getQuizAnswers(),user.getChatId());
        }
        else{
            answer ="Something wrong.(End REG P default text";
            cache.removeById(message.getChatId());
        }
        return  answer;
    }

    private String inputFullName(Message message){
        String answer;
        if (message.hasText()) {
            user.setName(message.getText());
            user.setPosition(UserPosition.INPUT_AGE);
            answer = "Type your age";
        }
        else {
            answer = "Name was incorrect. Please type again.";
        }
        return answer;
    }

    private String inputAge(Message message){
        String answer;
        try {
            user.setAge( Integer.parseInt(message.getText()));
            user.setPosition(UserPosition.INPUT_CITY);
            answer = "Type your City";
        } catch (IllegalArgumentException e) {
            answer = "Age was incorrect. Please type again.";
            e.printStackTrace();
        }
        return answer;
    }

    private String inputCity(Message message){
        String answer;
        if (message.hasText()) {
            user.setCity(message.getText());
            answer="Now quiz. Please, tap all buttons which topic you prefer to conversation\nand then tap <Ok, done it!> : ";
            user.setPosition(UserPosition.INPUT_QUIZ);
        }
        else {
            answer = "City was incorrect. Please type again.";
        }
        return answer;
    }
}
