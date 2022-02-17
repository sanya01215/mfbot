package com.mfgroup.tgbot.service.user;

import com.mfgroup.tgbot.dao.UserRepository;
import com.mfgroup.tgbot.domain.user.User;
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
    public User findBestTagMatchUser(List<String> matchTagSet, Long currentUserChatId){
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
        return bestMatchUser;
    }

}
