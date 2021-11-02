package com.MFGroup.MFTelegramBot.domain;

import org.springframework.data.annotation.Id;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
public class User {
    @Id
    private String id;
    private Long chatId;
    private String username;
    private String name;
    private int age;
    private String city;
    private Date regDate;
    private Position position;

    private Map<String,Boolean> quizAnswers;
    public User() {
        quizAnswers = new LinkedHashMap<>();
        quizAnswers.put("eng", false);
        quizAnswers.put("ukr", false);
        quizAnswers.put("rus", false);
        quizAnswers.put("it", false);
        quizAnswers.put("psychology", false);
        quizAnswers.put("sport", false);
        quizAnswers.put("drawing", false);
        quizAnswers.put("nature", false);
        quizAnswers.put("business", false);
        quizAnswers.put("cryptocurrency", false);
        quizAnswers.put("travel", false);
        quizAnswers.put("law", false);
    }

    public User(String username, Long chatId, String name) {
        regDate = Calendar.getInstance().getTime();
        this.username = username;
        this.name = name;
        this.chatId = chatId;
        quizAnswers = new LinkedHashMap<>();
        quizAnswers.put("eng", false);
        quizAnswers.put("ukr", false);
        quizAnswers.put("rus", false);
        quizAnswers.put("it", false);
        quizAnswers.put("psychology", false);
        quizAnswers.put("sport", false);
        quizAnswers.put("drawing", false);
        quizAnswers.put("nature", false);
        quizAnswers.put("business", false);
        quizAnswers.put("cryptocurrency", false);
        quizAnswers.put("travel", false);
        quizAnswers.put("law", false);
    }


    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Date getRegDate() {
        return regDate;
    }


    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Map<String, Boolean> getQuizAnswers() {
        return quizAnswers;
    }

    @Override
    public String toString() {
        return String.format(
                "Client %s, username @%s, with chatId %s 's been reg at %tT %tD %n . His tags: \n %s",
                name, username, chatId, regDate, regDate, quizAnswers);
    }
}
