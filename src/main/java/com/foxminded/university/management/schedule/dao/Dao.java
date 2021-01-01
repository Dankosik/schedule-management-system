package com.foxminded.university.management.schedule.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> getById(Long id);

    List<T> getAll();

    T save(T object);

    boolean delete(T object);

    List<T> saveAll(List<T> objects);
}
