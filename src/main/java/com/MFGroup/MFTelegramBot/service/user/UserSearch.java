package com.MFGroup.MFTelegramBot.service.user;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSearch {
    @Autowired
    private UserRepository userRepo;
    public User findBestTagMatchUser(List<String> matchTagSet, Long currentUserChatId){
        User bestMatchUser=null;
        int bestMatchUserPoints=0;
        List<User> allUsers ;

//        for (User newBestMatchUser : allUsers){
//            if (Objects.equals(newBestMatchUser.getName(), userRepo.findById(currentUserChatId)))continue;
//            int newBestMatchUserPoints=0;
//
//            for(String s : newBestMatchUser.getQuizAnswers()){
//                if(matchTagSet.contains(s))newBestMatchUserPoints++;
//            }
//            if(bestMatchUserPoints<newBestMatchUserPoints){
//                bestMatchUser=newBestMatchUser;
//                bestMatchUserPoints=newBestMatchUserPoints;
//            }
//        }
        return bestMatchUser;
    }

}
