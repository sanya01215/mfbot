//package com.MFGroup.MFTelegramBot.serviceTest;
//
//import com.MFGroup.MFTelegramBot.cache.Cache;
//import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
//import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
//import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
//import com.MFGroup.MFTelegramBot.model.User;
//import com.MFGroup.MFTelegramBot.service.message.impl.MessageService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.telegram.telegrambots.meta.api.objects.Chat;
//import org.telegram.telegrambots.meta.api.objects.Message;
//
//import static com.MFGroup.MFTelegramBot.cache.BotData.MessageHandlerSpeech.START_HELLO;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//public class MessageServiceTest {
//    @Mock
//    private  KeyboardFactory keyBoard;
//    @Mock
//    private SendMsgEditMsgDecorator messageToSend;
//    @Mock
//    private  Cache<User> cache;
//    @InjectMocks
//    private MessageService messageService;
//
//    @Test
//    public void answerToStartMessageIsStartHello(){
//        long expectChatId = 123L;
//        //init received message
//        SendMsgWrapper expectSendMsg = new SendMsgWrapper();
//        expectSendMsg.setText(START_HELLO);
//        expectSendMsg.setChatId(Math.toIntExact(expectChatId));
//
//        //init send message
//        Message testMsg = new Message();
//        Chat tesChat = new Chat();
//        tesChat.setId(expectChatId);
//        testMsg.setText("/start");
//        testMsg.setChat(tesChat);
//
//        assertThat(messageService.processReceivedObj(testMsg).contains(expectSendMsg)).isTrue();
//    }
//
//}
