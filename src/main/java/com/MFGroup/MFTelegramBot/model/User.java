package com.MFGroup.MFTelegramBot.model;

import lombok.Builder;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Data
@Builder
public class User {
    private Long chatId;
    private String username;
    private String inTelegramName;
    private String inTelegramBio;
    private Date regDate;

    private String name;
    private int age;
    private String city;
    private UserPositionEnum position;
    private String tags;

    private List<String> quizAnswers;

    public User(String username, Long chatId) {
        regDate = Calendar.getInstance().getTime();
        this.username = username;
        this.chatId = chatId;
    }

    public User() {
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
