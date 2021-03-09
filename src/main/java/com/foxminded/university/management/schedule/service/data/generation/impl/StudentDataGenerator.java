package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentDataGenerator implements DataGenerator<Student> {
    private final List<String> firstNames =
            List.of("John", "Liam", "Mason", "Jacob", "William", "Ethan", "Michael", "Daniel", "Edward", "Mark",
                    "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Lisa", "Carol");
    private final List<String> lastNames =
            List.of("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
                    "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson");

    @Override
    public List<Student> generateData() {
        List<Student> result = new ArrayList<>();
        List<Long> groupIds = ReceivingIdUtils.getGroupIds();
        int max = firstNames.size() - 1;
        for (int i = 0; i < RandomUtils.random(1, firstNames.size() - 1); i++) {
            Student student = new Student();
            String firstName = firstNames.get(RandomUtils.random(0, max));
            String lastName = lastNames.get(RandomUtils.random(0, max));
            String middleName = lastNames.get(RandomUtils.random(0, max));
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setMiddleName(middleName);
            student.setCourseNumber(RandomUtils.random(1, 4));
            student.setGroupId(groupIds.get(RandomUtils.random(0, groupIds.size() - 1)));
            result.add(student);

        }
        return result;
    }
}
