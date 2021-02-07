package com.foxminded.university.management.schedule.service.data.generation;

import java.util.List;

public interface DataGenerator<T> {
    List<T> generateData();
}
