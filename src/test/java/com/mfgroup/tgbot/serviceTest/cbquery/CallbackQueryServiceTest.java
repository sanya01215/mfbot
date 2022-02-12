package com.mfgroup.tgbot.serviceTest.cbquery;

import com.mfgroup.tgbot.cache.BotData;
import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.keyboard.KeyboardFactory;
import com.mfgroup.tgbot.model.message.adapter.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.model.user.User;
import com.mfgroup.tgbot.service.receive.handler.cbquery.CallbackQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CallbackQueryServiceTest {
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
        String testTravel = "travel";
        String testLaw = "law";
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
//perform user add 2 tags
        receivedCbQuery.setData(testLaw);
        callbackQueryService.handleReceivedObj(receivedCbQuery,user);
        receivedCbQuery.setData(testTravel);
        SendMsgEditMsgAdapter sentTestMsg =callbackQueryService.handleReceivedObj(receivedCbQuery,user);
//check if tags saved and present in msg(edited)
        assertThat(sentTestMsg.getText()).isEqualTo(expectMsg + testLaw + delimiter + testTravel);
//perform remove 1 tag
        receivedCbQuery.setData(testRemove);
        callbackQueryService.handleReceivedObj(receivedCbQuery,user);
//check if tags -1 performed
        assertThat(sentTestMsg.getText()).isEqualTo(expectMsg + testLaw + delimiter + testTravel);
    }
}
