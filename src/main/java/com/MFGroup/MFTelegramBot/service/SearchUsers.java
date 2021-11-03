package com.MFGroup.MFTelegramBot.service;

import com.MFGroup.MFTelegramBot.domain.User;
import com.MFGroup.MFTelegramBot.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class SearchUsers {
    @Autowired
    private UserRepository userRepo;
    public User findBestTagMatchUser(Set<String> matchTagSet, Long currentUserChatId){
        User bestMatchUser=null;
        int bestMatchUserPoints=0;
        List<User> allUsers = userRepo.findAll();

        for (User newBestMatchUser : allUsers){
            if (Objects.equals(newBestMatchUser.getName(), userRepo.findById(String.valueOf(currentUserChatId))))continue;
            int newBestMatchUserPoints=0;

            for(String s : newBestMatchUser.getQuizAnswers()){
                if(matchTagSet.contains(s))newBestMatchUserPoints++;
            }
            if(bestMatchUserPoints<newBestMatchUserPoints){
                bestMatchUser=newBestMatchUser;
                bestMatchUserPoints=newBestMatchUserPoints;
            }
        }
        return bestMatchUser;
    }

}
