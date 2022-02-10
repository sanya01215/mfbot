package com.mfgroup.tgBot.cache;

import java.util.List;

public interface Cache <T>{
    T findById(Long id);
    void removeById(Long id);
    void deleteAll();
    List<T> getAll();
    void add(T t);
}
