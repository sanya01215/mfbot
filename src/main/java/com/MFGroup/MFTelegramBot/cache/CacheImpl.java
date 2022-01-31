package com.MFGroup.MFTelegramBot.cache;

import com.MFGroup.MFTelegramBot.dao.UserRepository;
import com.MFGroup.MFTelegramBot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheImpl implements Cache<User> {

    UserRepository userRepo;
    public CacheImpl(@Autowired UserRepository userRepo) {
        this.userRepo = userRepo;
        retrieveDataFromDB();
    }

    private Map<Long, User> users;
    public CacheImpl() {
        this.users = new HashMap<>();
    }


    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public void removeById(Long id) {
        users.remove(id);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values()) {
        };
    }

    @Override
    public void add(User user) {
    if(user.getChatId() != null)users.put(user.getChatId(),user);
    else throw new NullPointerException("No id");
    }
    public void retrieveDataFromDB(){
        List<User> retrievedUsers = userRepo.findAll();
        users = new HashMap<>();
        retrievedUsers.forEach(n -> users.put(n.getChatId(),n));
    }
    public void saveDataToBD(){
        users.forEach((key, value) -> userRepo.save(value));
    }
}
