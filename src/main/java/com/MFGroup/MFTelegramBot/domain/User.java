package com.MFGroup.MFTelegramBot.domain;

import org.springframework.data.annotation.Id;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class User {
    @Id
    private String id;
    private Long chatId;
    private String username;
    private Date regDate;

    private String name;
    private int age;
    private String city;
    private Position position;
    private String tags;

    private Set<String> quizAnswers;

    public User(String username, Long chatId) {
        regDate = Calendar.getInstance().getTime();
        this.username = username;
        this.chatId = chatId;
    }

    public User() {
    }

    @Override
    public String toString() {
        if(tags==null) {
            StringBuilder tagsSb = new StringBuilder();
            for (String s : quizAnswers) {
                tagsSb.append(s).append(" || ");
            }
            tags=tagsSb.toString();
        }
        return String.format(
                "\nClient: %s\n Username: @%s\n ChatId: %s\n Reg time: %tT %tD \n His tags: \n %s\n",
                name, username, chatId, regDate, regDate, tags);
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Set<String> getQuizAnswers() {
        return quizAnswers;
    }

    public void setQuizAnswers(Set<String> quizAnswers) {
        this.quizAnswers = quizAnswers;
    }
}
