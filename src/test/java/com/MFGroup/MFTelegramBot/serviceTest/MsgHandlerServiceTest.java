package com.MFGroup.MFTelegramBot.serviceTest;

import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.impl.MsgHandlerService;
import com.MFGroup.MFTelegramBot.service.msgsender.MsgSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.MFGroup.MFTelegramBot.dao.BotData.MessageHandlerSpeech.START_HELLO;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MsgHandlerServiceTest {
    @Mock
    private  KeyboardFactory keyBoard;
    @Mock
    private MsgSender msgSender;
    @Mock
    private SendMsgEditMsgDecorator messageToSend;
    @Mock
    private  Cache<User> cache;
    @InjectMocks
    private MsgHandlerService msgHandlerService;

    @Test
    public void answerToStartMessageIsStartHello(){
        long expectChatId = 123L;
        //init received message
        SendMsgWrapper expectSendMsg = new SendMsgWrapper();
        expectSendMsg.setText(START_HELLO);
        expectSendMsg.setChatId(Math.toIntExact(expectChatId));

        //init send message
        Message testMsg = new Message();
        Chat tesChat = new Chat();
        tesChat.setId(expectChatId);
        testMsg.setText("/start");
        testMsg.setChat(tesChat);

        msgHandlerService.processReceivedObj(testMsg);
        verify(msgSender).sendMessage(expectSendMsg);
    }

}
