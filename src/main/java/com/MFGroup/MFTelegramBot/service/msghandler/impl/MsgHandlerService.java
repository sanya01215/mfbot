package com.MFGroup.MFTelegramBot.service.msghandler.impl;


import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.dao.BotData;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.Handler;
import com.MFGroup.MFTelegramBot.service.msgsender.MsgSender;
import com.MFGroup.MFTelegramBot.service.userhandler.UserSearch;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import static com.MFGroup.MFTelegramBot.dao.BotData.MessageHandlerSpeech.*;


@Component
public class MsgHandlerService implements Handler<Message> {

    private final KeyboardFactory keyBoard;

    private final UserRepository userRepo;

    public final MsgSender msgSender;

    public final UserSearch userSearch;

    private SendMsgEditMsgDecorator messageToSend;

    private final Cache<User> cache;

    private final RegMsgHandler regMsgHandler;

    public MsgHandlerService(KeyboardFactory keyBoard, UserRepository userRepo, MsgSender msgSender, UserSearch userSearch, Cache<User> cache, RegMsgHandler regMsgHandler) {
        this.keyBoard = keyBoard;
        this.userRepo = userRepo;
        this.msgSender = msgSender;
        this.userSearch = userSearch;
        this.messageToSend = new SendMsgWrapper();
        this.cache = cache;
        this.regMsgHandler = regMsgHandler;
    }

    @Override
    public void processReceivedObj(Message message) {
        String receivedMsg = message.getText().trim();
        String chatId = String.valueOf(message.getChatId());
        User user = cache.findById(message.getChatId());
        //for new users (start session)
        if (user != null)throw new RuntimeException("Cached user on start position");
            switch (receivedMsg) {
                case "/start":
                    prepareMsgAndSend(chatId, START_HELLO, keyBoard.removeReplyKeyBoard());
                    prepareMsgAndSend(chatId, WAIT_CHOOSE, keyBoard.getContinueCancelReplyKeyBoard());
                    break;

                case "Ok":
                    prepareMsgAndSend(chatId, TYPE_NAME, keyBoard.removeReplyKeyBoard());
                    cache.add(getNewUserFromMsg(message));
                    break;

                default:
                    messageToSend.setText(PROBLEM);
                    prepareMsgAndSend(chatId, PROBLEM, keyBoard.removeReplyKeyBoard());
            }
        }

    private User getNewUserFromMsg(Message message) {
        Chat chat = message.getChat();
        return User.builder()
                .username(chat.getUserName())
                .inTelegramName(chat.getFirstName() + chat.getLastName())
                .inTelegramBio(chat.getBio())
                .position(BotData.UserPositionEnum.INPUT_FULLNAME)
                .chatId(chat.getId())
                .build();
    }

    private void prepareMsgAndSend(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMsgWrapper messageToSend = new SendMsgWrapper();
        messageToSend.setChatId(chatId);
        messageToSend.setText(text);
        messageToSend.setReplyMarkup(replyKeyboard);
        msgSender.sendMessage(messageToSend);
    }
}
