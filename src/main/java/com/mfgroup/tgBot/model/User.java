package com.mfgroup.tgBot.model;

import com.mfgroup.tgBot.cache.BotData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long chatId;
    private String username;
    private String inTelegramName;
    private String inTelegramBio;
    private Date regDate;

    private String name;
    private int age;
    private String city;
    private BotData.UserPositionEnum position;
    private String tags;
    private Long lastMsgId;

    @ElementCollection(fetch = FetchType.EAGER) // 1
    @CollectionTable(name = "answers_list", joinColumns = @JoinColumn(name = "id")) // 2
    @Column(name = "answers") // 3
    private List<String> quizAnswers;

    public User(String username, Long chatId) {
        regDate = Calendar.getInstance().getTime();
        this.username = username;
        this.chatId = chatId;
    }
    public User(Long chatId, BotData.UserPositionEnum userPos) {
        regDate = Calendar.getInstance().getTime();
        this.chatId = chatId;
        this.setPosition(userPos);
    }


    public User() {
        regDate = Calendar.getInstance().getTime();
    }

    @Override
    public String toString() {
        if (tags == null && quizAnswers!=null) {
            StringBuilder tagsSb = new StringBuilder();
            for (String s : quizAnswers) {
                tagsSb.append(s).append(" || ");
            }
            tags = tagsSb.toString();
        }
        return String.format(
                "\nPerson: %s\n Telegram name %s\n Username: @%s\n ChatId: %s\n Telegram Bio: %s\n Reg time: %tT %tD \n His tags: \n %s\n",
                name, inTelegramName, username, chatId,inTelegramBio ,regDate, regDate, tags);
    }
}
