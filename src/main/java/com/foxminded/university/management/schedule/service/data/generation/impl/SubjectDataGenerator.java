package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class SubjectDataGenerator implements DataGenerator<Subject> {
    private final List<String> subjectNames = new LinkedList<>(Arrays.asList("Math", "Art", "Biology", "Programming", "Music", "Economy",
            "Psychology", "Physics", "Philosophy", "History"));

    @Override
    public List<Subject> generateData() {
        List<Subject> result = new ArrayList<>();
        int sizeOfSubjects = subjectNames.size();
        for (int i = 0; i < RandomUtils.random(1, sizeOfSubjects); i++) {
            Subject subject = new Subject();
            subject.setUniversityId(1L);
            subject.setName(generateSubjectName());
            result.add(subject);
        }
        return result;
    }

    private String generateSubjectName() {
        String subjectName = subjectNames.get(RandomUtils.random(0, subjectNames.size() - 1));
        subjectNames.remove(subjectName);
        return subjectName;
    }
}
