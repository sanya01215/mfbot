package com.MFGroup.MFTelegramBot.handler.registration.quiz;

import java.util.*;

public class EntryQuiz {
    private Date quizTime;

    public EntryQuiz() {
        this.quizTime = Calendar.getInstance().getTime();
        this.quizAnswers = new TreeMap<>();
    }

    private   Map<String,Boolean> quizAnswers;
    {
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
    public Date getQuizTime() {
        return quizTime;
    }

    public void setQuizTime(Date quizTime) {
        this.quizTime = quizTime;
    }

    public Map<String, Boolean> getQuizAnswers() {
        return quizAnswers;
    }
}
