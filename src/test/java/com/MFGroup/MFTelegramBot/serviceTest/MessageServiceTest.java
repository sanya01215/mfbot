package com.MFGroup.MFTelegramBot.serviceTest;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrap;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.service.message.impl.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.START_HELLO;
import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.WAIT_CHOOSE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private KeyboardFactory keyBoard;
    @Mock
    private UserRepository userRepo;
    @Mock
    private SendMsgEditMsgDecorator sendMessage;
    @InjectMocks
    private MessageService messageService;

    @Test
    public void answerToStartMessageIsStartHello(){
        long expectChatId = 123L;
        String expectChatIdStr ="123";
        String expectText = START_HELLO+"\n"+WAIT_CHOOSE;

        //init send message
        Message testMsg = new Message();
        Chat tesChat = new Chat();
        tesChat.setId(expectChatId);
        testMsg.setText("/start");
        testMsg.setChat(tesChat);

        //perform receive test message
        SendMsgWrap retrievedAnswer = (SendMsgWrap) messageService.processReceivedObj(testMsg);
        assertThat(retrievedAnswer.getChatId()).isEqualTo(expectChatIdStr);
        assertThat(retrievedAnswer.getText()).isEqualTo(expectText);
    }

}
