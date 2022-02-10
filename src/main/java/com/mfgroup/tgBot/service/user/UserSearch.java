package com.mfgroup.tgBot.service.user;

import com.mfgroup.tgBot.dao.UserRepository;
import com.mfgroup.tgBot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserSearch {
    @Autowired
    private UserRepository userRepo;
    @Transactional
    public String findBestTagMatchUser(List<String> matchTagSet, Long currentUserChatId){
        User bestMatchUser=null;
        User givenUser = userRepo.getById(currentUserChatId);
        int bestMatchUserPoints=0;
        List<User> allUsers = userRepo.findAll();

        for (User newBestMatchUser : allUsers){
            if (Objects.equals(newBestMatchUser.getName(), givenUser.getName()))continue;
            int newBestMatchUserPoints=0;

            for(String s : newBestMatchUser.getQuizAnswers()){
                if(matchTagSet.contains(s))newBestMatchUserPoints++;
            }
            if(bestMatchUserPoints<newBestMatchUserPoints){
                bestMatchUser=newBestMatchUser;
                bestMatchUserPoints=newBestMatchUserPoints;
            }
        }
        String s = bestMatchUser.toString();
        String b =s;
        return s;
    }

}
