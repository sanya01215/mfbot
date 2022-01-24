package com.MFGroup.MFTelegramBot.service.handler;


import com.MFGroup.MFTelegramBot.controller.MessageSender;
import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.model.UserPositionEnum;
import com.MFGroup.MFTelegramBot.service.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static com.MFGroup.MFTelegramBot.model.BotData.MessageHandlerSpeech.*;


@Component
public class MessageHandler implements Handler<Message> {

    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final MessageSender messageSender;

    public final UserSearch userSearch;

    private SendMessage messageToSend;

    private final Cache<User> cache;

    private final RegMessageHandler regMessageHandler;

    public MessageHandler(KeyboardFactory keyBoard, UserRepository userRepo, MessageSender messageSender, UserSearch userSearch, Cache<User> cache, RegMessageHandler regMessageHandler) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.messageSender = messageSender;
        this.userSearch = userSearch;
        this.messageToSend = new SendMessage();
        this.cache = cache;
        this.regMessageHandler = regMessageHandler;
    }

    @Override
    public void choose(Message message) {
        String receivedMsg = message.getText().trim();
        String chatId = message.getChatId().toString();

        User user = cache.findById(message.getChatId());

        //for new users (start session)
        if (user == null) {
            switch (receivedMsg) {
                case "/start":
                    prepareMessageAndSend(messageSender,chatId,START_HELLO, keyBoard.removeReplyKeyBoard());
                    messageSender.sendMessage(messageToSend);
                    prepareMessageAndSend(messageSender,chatId,WAIT_CHOOSE,keyBoard.getContinueCancelReplyKeyBoard());
                    break;

                case "Ok":
                    prepareMessageAndSend(messageSender,chatId,TYPE_NAME,keyBoard.removeReplyKeyBoard());
                    cache.add(getNewUser(message));
                    break;

                default:
                    messageToSend.setText(PROBLEM);
            }
            messageSender.sendMessage(messageToSend);
        }

    }

    private User getNewUser(Message message) {
        Chat chat = message.getChat();
        return User.builder()
                .username(chat.getUserName())
                .inTelegramName(chat.getFirstName() + chat.getLastName())
                .inTelegramBio(chat.getBio())
                .position(UserPositionEnum.INPUT_FULLNAME)
                .chatId(chat.getId())
                .build();
    }

    private void prepareMessageAndSend(MessageSender messageSender, String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage messageToSend = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .build();
        messageSender.sendMessage(messageToSend);
    }
}
