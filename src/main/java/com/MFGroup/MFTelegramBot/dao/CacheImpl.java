package com.MFGroup.MFTelegramBot.dao;

import com.MFGroup.MFTelegramBot.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheImpl implements Cache<User> {
    private final Map<Long, User> users;

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
    public List<User> getAll() {
        return new ArrayList<>(users.values()) {
        };
    }

    @Override
    public void add(User user) {
    if(user.getChatId() != null){
        users.put(user.getChatId(),user);
    }
    else{
        throw new NullPointerException("No id");
    }
    }
}
