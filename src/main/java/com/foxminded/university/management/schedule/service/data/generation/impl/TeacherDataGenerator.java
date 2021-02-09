package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherDataGenerator implements DataGenerator<Teacher> {
    private final List<String> firstNames =
            List.of("John", "Liam", "Mason", "Jacob", "William", "Ethan", "Michael", "Daniel", "Edward", "Mark",
                    "Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Lisa", "Carol");
    private final List<String> lastNames =
            List.of("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
                    "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin", "Thompson", "Garcia", "Martinez", "Robinson");

    @Override
    public List<Teacher> generateData() {
        List<Teacher> result = new ArrayList<>();
        List<Long> facultyIds = ReceivingIdUtils.getFacultyIds();
        int min = 0;
        int max = firstNames.size();
        for (int i = 0; i < 1 + (int) (Math.random() * firstNames.size() - 1); i++) {
            Teacher teacher = new Teacher();
            String firstName = firstNames.get((min + (int) (Math.random() * max)));
            String lastName = lastNames.get((min + (int) (Math.random() * max)));
            String middleName = lastNames.get((min + (int) (Math.random() * max)));
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setMiddleName(middleName);
            teacher.setUniversityId(1L);
            teacher.setFacultyId(facultyIds.get((int) (1 + (Math.random() * facultyIds.size() - 1))));
            result.add(teacher);

        }
        return result;
    }
}
