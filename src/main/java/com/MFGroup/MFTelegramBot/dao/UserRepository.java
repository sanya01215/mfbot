package com.MFGroup.MFTelegramBot.dao;

import com.MFGroup.MFTelegramBot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
    public User findByUsername(String username);
}
