package com.MFGroup.MFTelegramBot.cache;

import java.util.List;

public interface Cache <T>{
    T findById(Long id);
    void removeById(Long id);
    List<T> getAll();
    void add(T t);
}
