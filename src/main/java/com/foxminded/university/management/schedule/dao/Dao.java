package com.foxminded.university.management.schedule.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> getById(Long id);

    List<T> getAll();

    T save(T object);

    boolean delete(T object);

    List<T> saveAll(List<T> objects);
}
