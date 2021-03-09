package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils.getFacultyIds;

@Service
public class GroupDataGenerator implements DataGenerator<Group> {
    @Override
    public List<Group> generateData() {
        List<Group> result = new ArrayList<>();
        List<Long> facultyIds = getFacultyIds();
        for (int i = 0; i < RandomUtils.random(5, 10); i++) {
            Group group = new Group();
            String groupName = generateFirstTwoCharacters() + "-" + generateTwoDigits();
            group.setName(groupName);
            group.setFacultyId(facultyIds.get(RandomUtils.random(0, facultyIds.size() - 1)));
            result.add(group);
        }
        return result;
    }

    private String generateFirstTwoCharacters() {
        char[] chars = "ABCDIFGHIJKLMNOPRSTUVWXYZ".toCharArray();
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            char c = chars[random.nextInt(chars.length)];
            result.append(c);
        }
        return result.toString();
    }

    private String generateTwoDigits() {
        StringBuilder result = new StringBuilder();
        int min = 0;
        int max = 9;
        for (int i = 0; i < 2; i++) {
            result.append(RandomUtils.random(min, max));
        }
        return result.toString();
    }
}
