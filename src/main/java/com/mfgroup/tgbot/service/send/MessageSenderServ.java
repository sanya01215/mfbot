package com.mfgroup.tgbot.service.send;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.model.user.User;
import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.model.message.adapter.impl.EditMsgAdapter;
import com.mfgroup.tgbot.model.message.adapter.impl.SendMsgAdapter;
import com.mfgroup.tgbot.client.BotClient;
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
    public void sendAnswer(SendMsgEditMsgAdapter replyMsg) {
        Message sentMessage = null;
        if (replyMsg.getClass() == EditMsgAdapter.class) botClient.execute((EditMsgAdapter) replyMsg);
        if (replyMsg.getClass() == SendMsgAdapter.class) sentMessage = botClient.execute((SendMsgAdapter) replyMsg);


        if (sentMessage != null) saveLastMessageId(sentMessage.getMessageId(), sentMessage.getChatId());
    }
    private void saveLastMessageId(int sentMessageId,long chatId) {
        User currentUser = userRepo.findById(chatId).orElseThrow(()->new RuntimeException("No user to save last message id"));
        currentUser.setLastMsgId((long) sentMessageId);
        userRepo.saveAndFlush(currentUser);
    }

}
