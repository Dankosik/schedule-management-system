package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TeacherRepositoryTest extends BaseDaoTest {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnTeacherWithIdOne() {
        Teacher actual = teacherRepository.findById(1000L).get();
        Teacher expected = new Teacher(1000L, "Hillel", "St. Leger", "Lugard",
                entityManager.find(Teacher.class, 1000L).getFaculty(), entityManager.find(Teacher.class, 1000L).getLectures());

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfTeachers() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard",
                        entityManager.find(Teacher.class, 1000L).getFaculty(),
                        entityManager.find(Teacher.class, 1000L).getLectures()),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey",
                        entityManager.find(Teacher.class, 1001L).getFaculty(),
                        entityManager.find(Teacher.class, 1001L).getLectures()));
        List<Teacher> actual = teacherRepository.findAll();

        assertTrue(actual.containsAll(expected));
    }
}
