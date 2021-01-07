package com.foxminded.university.management.schedule.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends BaseEntity<K>, K> {
    Optional<T> getById(K id);

    List<T> getAll();

    T save(T object);

    boolean deleteById(K id);

    List<T> saveAll(List<T> objects);
}
