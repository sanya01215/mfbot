package com.MFGroup.MFTelegramBot.controller.impl;

import com.MFGroup.MFTelegramBot.controller.BotClientController;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrapper;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.bot.BotClient;
import com.MFGroup.MFTelegramBot.service.message.impl.CallbackQueryService;
import com.MFGroup.MFTelegramBot.service.message.impl.RegMessageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotClientControllerImpl implements BotClientController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CallbackQueryService callbackQueryService;
    @Autowired
    private RegMessageService regMessageService;
    @Autowired
    @Lazy
    //fix circle dependency botClient-BotClientController
    private BotClient botClient;

    @Override
    public void receiveMsg(Message message) {
        SendMsgEditMsgDecorator replyMsgList = null;
        replyMsgList = regMessageService.processReceivedObj(message);
        sendAnswer(replyMsgList);
    }

    @Override
    public void receiveCBQuery(CallbackQuery callbackQuery) {
        SendMsgEditMsgDecorator replyMsg = callbackQueryService.processReceivedObj(callbackQuery);
        sendAnswer(replyMsg);
    }

    @SneakyThrows
    private void sendAnswer(SendMsgEditMsgDecorator replyMsgList) {
        Message sentMessage = null;
        if (replyMsgList.getClass() == EditMsgWrapper.class) botClient.execute((EditMsgWrapper) replyMsgList);
        if (replyMsgList.getClass() == SendMsgWrapper.class) sentMessage = botClient.execute((SendMsgWrapper) replyMsgList);

        if (sentMessage != null)saveLastMessageId(sentMessage.getMessageId(),sentMessage.getChatId());
    }

    @Override
    public void process(Update update) {
        if (update.hasMessage()) {
            receiveMsg(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            receiveCBQuery(update.getCallbackQuery());
        }
    }

    private void saveLastMessageId(int sentMessageId,long chatId) {
        User currentUser = userRepo.findById(chatId).orElseThrow(()->new RuntimeException("No user to save last message id"));
            currentUser.setLastMsgId((long) sentMessageId);
            userRepo.saveAndFlush(currentUser);
    }
}
