package com.mfgroup.tgBot.service.send;

import com.mfgroup.tgBot.dao.UserRepository;
import com.mfgroup.tgBot.model.User;
import com.mfgroup.tgBot.model.decorator.SendMsgEditMsgDecorator;
import com.mfgroup.tgBot.model.decorator.impl.EditMsgWrap;
import com.mfgroup.tgBot.model.decorator.impl.SendMsgWrap;
import com.mfgroup.tgBot.view.BotClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
public class MessageSenderServ {
    private final UserRepository userRepo;
    private final BotClient botClient;

    public MessageSenderServ(UserRepository userRepo, BotClient botClient) {
        this.userRepo = userRepo;
        this.botClient = botClient;
    }
    @SneakyThrows
    public void sendAnswer(SendMsgEditMsgDecorator replyMsg) {
        Message sentMessage = null;
        if (replyMsg.getClass() == EditMsgWrap.class) botClient.execute((EditMsgWrap) replyMsg);
        if (replyMsg.getClass() == SendMsgWrap.class) sentMessage = botClient.execute((SendMsgWrap) replyMsg);


        if (sentMessage != null) saveLastMessageId(sentMessage.getMessageId(), sentMessage.getChatId());
    }
    private void saveLastMessageId(int sentMessageId,long chatId) {
        User currentUser = userRepo.findById(chatId).orElseThrow(()->new RuntimeException("No user to save last message id"));
        currentUser.setLastMsgId((long) sentMessageId);
        userRepo.saveAndFlush(currentUser);
    }

}
