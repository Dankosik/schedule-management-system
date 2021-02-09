package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FacultyDataGenerator implements DataGenerator<Faculty> {
    @Override
    public List<Faculty> generateData() {
        List<Faculty> result = new ArrayList<>();
        for (int i = 0; i < 1 + (int) (Math.random() * 10); i++) {
            Faculty faculty = new Faculty();
            faculty.setUniversityId(1L);
            faculty.setName(generateFacultyName());
            result.add(faculty);
        }
        return result;
    }

    private String generateFacultyName() {
        char[] chars = "ABCDIFGHIJKLMNOPRSTUVWXYZ".toCharArray();
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 1 + (int) (Math.random() * 6); i++) {
            char c = chars[random.nextInt(chars.length)];
            result.append(c);
        }
        return result.toString();
    }
}
