package com.MFGroup.MFTelegramBot.serviceTest;

import com.MFGroup.MFTelegramBot.dao.BotData;
import com.MFGroup.MFTelegramBot.dao.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.SendMsgWrapper;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.msghandler.impl.CallbackQueryHandlerService;
import com.MFGroup.MFTelegramBot.service.msgsender.MsgSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static com.MFGroup.MFTelegramBot.dao.BotData.CallbackQueryHandlerSpeech.TAGS_DELIMITER;
import static com.MFGroup.MFTelegramBot.dao.BotData.CallbackQueryHandlerSpeech.YOUR_TAGS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallbackQueryHandlerServiceTest {
    @Mock
    private  SendMsgEditMsgDecorator editMessageText;
    @Mock
    private List<String> quizAnswers;
    @Mock
    private User user;
    @Mock
    private  KeyboardFactory keyboardFactory;
    @Mock
    private  MsgSender msgSender;
    @Mock
    private  Cache<User> cache;
    @Mock
    private  UserRepository userRepo;
    @InjectMocks
    private CallbackQueryHandlerService callbackQueryHandlerService;

    @Test
    public void answerToTagCallbackQueryInputIsEditMsgWithAllTags(){
        //testing data
        String testChatIdString = "123";
        int testChatIdInt = 123;
        long testChatIdLong = 123L;
        String testCQData = "eng";
        String expectEditText = YOUR_TAGS+"\n" + String.join(TAGS_DELIMITER, testCQData);

        //init chat
        Chat tesChat = new Chat();
        tesChat.setId(testChatIdLong);

        //msg init
        Message message = new Message();
        message.setMessageId(testChatIdInt);
        message.setChat(tesChat);

        //init received callbackQuery
        CallbackQuery testCallbackQuery = new CallbackQuery();
        testCallbackQuery.setId(testChatIdString);
        testCallbackQuery.setData(testCQData);
        testCallbackQuery.setMessage(message);

        //init send message
        SendMsgEditMsgDecorator expectEditMsg = new SendMsgWrapper();

        //in cache user init
        User user = new User();
        user.setChatId(testChatIdLong);
        user.setPosition(BotData.UserPositionEnum.INPUT_QUIZ);

        when(cache.findById(any())).thenReturn(user);

        //test service
        callbackQueryHandlerService.processReceivedObj(testCallbackQuery);
        verify(msgSender).prepareAndSendMsgOrEditMsg(expectEditMsg,testChatIdInt,expectEditText,keyboardFactory.getOkRemoveAttachKeyBoard());

    }

}
