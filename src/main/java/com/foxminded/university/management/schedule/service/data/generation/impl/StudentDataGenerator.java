package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
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
        int min = 0;
        int max = firstNames.size();
        for (int i = 0; i < 1 + (int) (Math.random() * firstNames.size() - 1); i++) {
            Student student = new Student();
            String firstName = firstNames.get((min + (int) (Math.random() * max)));
            String lastName = lastNames.get((min + (int) (Math.random() * max)));
            String middleName = lastNames.get((min + (int) (Math.random() * max)));
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setMiddleName(middleName);
            student.setUniversityId(1L);
            student.setCourseNumber((1 + (int) (Math.random() * 4)));
            student.setGroupId(groupIds.get((int) (1 + (Math.random() * groupIds.size() - 1))));
            result.add(student);

        }
        return result;
    }
}
