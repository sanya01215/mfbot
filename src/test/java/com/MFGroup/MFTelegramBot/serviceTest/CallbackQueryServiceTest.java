package com.MFGroup.MFTelegramBot.serviceTest;

import com.MFGroup.MFTelegramBot.cache.Cache;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.factory.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.message.impl.CallbackQueryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CallbackQueryServiceTest {
    @Mock
    private  SendMsgEditMsgDecorator editMessageText;
    @Mock
    private List<String> quizAnswers;
    @Mock
    private User user;
    @Mock
    private  KeyboardFactory keyboardFactory;
    @Mock
    private  Cache<User> cache;
    @Mock
    private  UserRepository userRepo;
    @InjectMocks
    private CallbackQueryService callbackQueryService;

//    @Test
//    public void answerToTagCallbackQueryInputIsEditMsgWithAllTags(){
//        //testing data
//        String testChatIdString = "123";
//        int testChatIdInt = 123;
//        long testChatIdLong = 123L;
//        String testCQData = "eng";
//        String expectEditText = YOUR_TAGS+"\n" + String.join(TAGS_DELIMITER, testCQData);
//
//        //init chat
//        Chat tesChat = new Chat();
//        tesChat.setId(testChatIdLong);
//
//        //msg init
//        Message message = new Message();
//        message.setMessageId(testChatIdInt);
//        message.setChat(tesChat);
//
//        //init received callbackQuery
//        CallbackQuery testCallbackQuery = new CallbackQuery();
//        testCallbackQuery.setId(testChatIdString);
//        testCallbackQuery.setData(testCQData);
//        testCallbackQuery.setMessage(message);
//
//        //init send message
//        SendMsgEditMsgDecorator expectEditMsg = new SendMsgWrapper();
//
//        //in cache user init
//        User user = new User();
//        user.setChatId(testChatIdLong);
//        user.setPosition(BotData.UserPositionEnum.INPUT_QUIZ);
//
//        when(cache.findById(any())).thenReturn(user);
//
//        //test service
//        callbackQueryHandlerService.processReceivedObj(testCallbackQuery);
//        verify(msgSender).prepareAndSendMsgOrEditMsg(expectEditMsg,testChatIdInt,expectEditText,keyboardFactory.getOkRemoveAttachKeyBoard());
//
//    }

}
