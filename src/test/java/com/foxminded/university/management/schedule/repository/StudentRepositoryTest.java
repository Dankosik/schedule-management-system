package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class StudentRepositoryTest extends BaseDaoTest {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnListOfStudents() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1,
                        entityManager.find(Student.class, 1000L).getGroup()),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1,
                        entityManager.find(Student.class, 1001L).getGroup()),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2,
                        entityManager.find(Student.class, 1002L).getGroup()),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2,
                        entityManager.find(Student.class, 1003L).getGroup()),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3,
                        entityManager.find(Student.class, 1004L).getGroup()));
        List<Student> actual = studentRepository.findAll();

        assertTrue(actual.containsAll(expected));
    }
}
