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
public class MessageHandler implements Handler<Message> {

    private final KeyBoardImpl keyBoard;

    private final UserRepository userRepo;

    public final MessageSender messageSender;

    public final UserSearch userSearch;

    private SendMessage sendMessage;

    private final Cache<User> cache;

    private final RegistrationQuiz registrationQuiz;

    private final RegMessageHandler regMessageHandler;

    public MessageHandler(KeyBoardImpl keyBoard, UserRepository userRepo, MessageSender messageSender, UserSearch userSearch, SendMessage sendMessage, Cache<User> cache, RegistrationQuiz registrationQuiz, RegMessageHandler regMessageHandler) {
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
        String receivedMsg = message.getText().trim();
        String chatId = message.getChatId().toString();
        sendMessage.setChatId(chatId);
        User user = cache.findById(message.getChatId());

        //for new users (start session)
        switch (receivedMsg) {
            case "/start":
                sendMessage.setText("Hello! There is The Bot for meeting some new interesting people : \n" +
                        "Please answer for a few questions, that help us to find you suitable person. ");
                sendMessage.setReplyMarkup(keyBoard.continueCancelKbInit());
                break;

            case "Ok. Let's go!":
                sendMessage.setText("Type your Full Name");
                User newUser = new User(message.getChat().getUserName(), message.getChatId());
                newUser.setPosition(UserPosition.INPUT_FULLNAME);
                newUser.setChatId(message.getChatId());
                cache.add(newUser);
                break;

            default:
                sendMessage.setText("Alright... Have a nice day!");
                break;
        }
        messageSender.sendMessage(sendMessage);
    }
}
