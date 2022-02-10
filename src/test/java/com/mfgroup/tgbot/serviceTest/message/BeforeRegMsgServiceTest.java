package com.mfgroup.tgbot.serviceTest.message;

import com.mfgroup.tgbot.cache.BotData;
import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.model.User;
import com.mfgroup.tgbot.model.decorator.impl.SendMsgWrap;
import com.mfgroup.tgbot.service.receive.handler.message.BeforeRegMsgService;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.mfgroup.tgbot.cache.BotData.MessageHandlerSpeech.START_HELLO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeforeRegMsgServiceTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    private MessageFactory msgFactory;
    @Mock
    public UserSearch userSearch;
    @InjectMocks
    private BeforeRegMsgService beforeRegMsgService;

    @Test
    public void answerToStartMessageIsStartHello() {
        long expectChatId = 123L;
        String expectChatIdStr = "123";
        String expectText = START_HELLO;
        
        //init send testMsg
        SendMsgWrap sendMsgWrap = new SendMsgWrap();
        sendMsgWrap.setText(expectText);
        sendMsgWrap.setChatId(expectChatIdStr);

        //init receive message
        Message testMsg = new Message();
        Chat tesChat = new Chat();
        tesChat.setId(expectChatId);
        testMsg.setText("/start");
        testMsg.setChat(tesChat);

        //init user
        User user = new User();
        user.setChatId(expectChatId);
        user.setPosition(BotData.UserPositionEnum.START);

        //mock msgFactory
        when(msgFactory.getStartMsg()).thenReturn(sendMsgWrap);

        //perform receive test message
        SendMsgWrap retrievedAnswer = (SendMsgWrap) beforeRegMsgService.handleReceivedObj(testMsg, user);
        assertThat(retrievedAnswer.getChatId()).isEqualTo(expectChatIdStr);
        assertThat(retrievedAnswer.getText()).isEqualTo(expectText);
    }

}
