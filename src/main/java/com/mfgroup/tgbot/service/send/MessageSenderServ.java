package com.mfgroup.tgbot.service.send;

import com.mfgroup.tgbot.botdata.BotData;
import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.adapter.message.impl.EditMsgAdapter;
import com.mfgroup.tgbot.adapter.message.impl.SendMsgAdapter;
import com.mfgroup.tgbot.client.BotClient;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void sendAnswer(SendMsgEditMsgAdapter replyMsg) {
        Message sentMessage = null;
        if (replyMsg.getClass() == EditMsgAdapter.class) botClient.execute((EditMsgAdapter) replyMsg);
        if (replyMsg.getClass() == SendMsgAdapter.class) sentMessage = botClient.execute((SendMsgAdapter) replyMsg);

        assert sentMessage != null;
        User currentUser = userRepo.findById(sentMessage.getChatId()).orElse(new User(sentMessage.getChatId(), BotData.UserPositionEnum.START));
        saveLastMessageId(sentMessage.getMessageId(), currentUser);
        userRepo.save(currentUser);
    }

    private void saveLastMessageId(long sentMsgId, User currentUser) {
        currentUser.setLastMsgId(sentMsgId);
    }

}
