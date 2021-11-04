package com.MFGroup.MFTelegramBot.model;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class RegistrationQuiz {
    private final Set<String> allTags;
    {
        allTags = new LinkedHashSet<>();
        allTags.add("eng");
        allTags.add("ukr");
        allTags.add("rus");
        allTags.add("it");
        allTags.add("psychology");
        allTags.add("sport");
        allTags.add("drawing");
        allTags.add("nature");
        allTags.add("business");
        allTags.add("cryptocurrency");
        allTags.add("travel");
        allTags.add("law");
    }

    public Set<String> getAllTags() {
        return allTags;
    }
}
