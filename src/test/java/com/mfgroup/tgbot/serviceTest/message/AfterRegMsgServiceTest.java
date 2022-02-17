package com.mfgroup.tgbot.serviceTest.message;

import com.mfgroup.tgbot.botdata.BotData;
import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.factory.message.MessageFactory;
import com.mfgroup.tgbot.adapter.message.SendMsgEditMsgAdapter;
import com.mfgroup.tgbot.domain.user.User;
import com.mfgroup.tgbot.service.receive.handler.message.AfterRegMsgService;
import com.mfgroup.tgbot.service.user.UserSearch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AfterRegMsgServiceTest {
    @Mock
    private UserRepository userRepo;
    @Mock
    public UserSearch userSearch;
    @Spy
    private MessageFactory msgFactory;
    @InjectMocks
    private AfterRegMsgService afterRegMsgService;

    @Test
    public void forCommandGetAllUsersAnswerArrayOfUsers() {
        //init test data
        String chatIdStr = "123";
        long chatIdL = 123L;
        String getUsersCommand = "Get all users";

        //init chat for msg
        Chat chat = new Chat();
        chat.setId(chatIdL);

        //init msg
        Message message = new Message();
        message.setChat(chat);
        message.setText(getUsersCommand);

        //init users
        User user = new User();
        user.setChatId(chatIdL);
        user.setPosition(BotData.UserPositionEnum.DONE_REGISTRATION);
        User user1 = new User();
        user.setChatId(222L);
        User user2 = new User();
        user.setChatId(333L);

        //init list of users
        List<User> userList = Arrays.asList(user, user1, user2);

        //set repo.getAll returns list of users
        when(userRepo.findAll()).thenReturn(userList);
        //set repo getbyid
        when(userRepo.getById(chatIdL)).thenReturn(user);
        //perform send command
        SendMsgEditMsgAdapter repliedMsg = afterRegMsgService.handleReceivedObj(message, chatIdL);

        //check if repliedMsgText equals users list of users
        assertThat(repliedMsg.getText()).isEqualTo(userList.toString());
    }
}
