package com.MFGroup.MFTelegramBot.serviceTest;

import com.MFGroup.MFTelegramBot.cache.BotData;
import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.decorator.SendMsgEditMsgDecorator;
import com.MFGroup.MFTelegramBot.decorator.impl.EditMsgWrap;
import com.MFGroup.MFTelegramBot.factory.keyboard.KeyboardFactory;
import com.MFGroup.MFTelegramBot.model.User;
import com.MFGroup.MFTelegramBot.service.message.impl.CallbackQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CallbackQueryServiceTest {
    @Mock
    private SendMsgEditMsgDecorator editMessageText;
    @Mock
    private User user;
    @Mock
    private KeyboardFactory keyboardFactory;
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private CallbackQueryService callbackQueryService;

    @Test
    public void answerToTagCallbackQueryInputIsEditMsgWithAllTags() {
// set data for test
        String testRemove = "REMOVE";
        String testTravel = "Travel";
        String testLaw = "Law";
        String expectMsg = "Your tags:\n";
        String delimiter = " || ";
        Long chatIdLong = 123L;
        Integer msgIdInt = 321;
//set chat for message
        Chat chat = new Chat();
        chat.setId(chatIdLong);
//set message for cbQuery
        Message message = new Message();
        message.setMessageId(msgIdInt);
        message.setChat(chat);
//set received cbQuery
        CallbackQuery receivedCbQuery = new CallbackQuery();
        receivedCbQuery.setMessage(message);
//set user for userRepo
        User user = new User();
        user.setPosition(BotData.UserPositionEnum.INPUT_QUIZ);
        user.setLastMsgId(321L);
//set userRepo return
        when(userRepo.getById(chatIdLong)).thenReturn(user);
//perform user add 2 tags
        receivedCbQuery.setData(testLaw);
        callbackQueryService.processReceivedObj(receivedCbQuery);
        receivedCbQuery.setData(testTravel);
        EditMsgWrap sentTestMsg = (EditMsgWrap) callbackQueryService.processReceivedObj(receivedCbQuery);
//check if tags saved and present in msg(edited)
        assertThat(sentTestMsg.getText()).isEqualTo(expectMsg + testLaw + delimiter + testTravel);
//perform remove 1 tag
        receivedCbQuery.setData(testRemove);
        callbackQueryService.processReceivedObj(receivedCbQuery);
//check if tags -1 performed
        assertThat(sentTestMsg.getText()).isEqualTo(expectMsg + testLaw + delimiter + testTravel);
    }
}
