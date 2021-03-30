package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class StudentDaoTest extends BaseDaoTest {
    private StudentDao studentDao;
    @Autowired
    private EntityManager entityManager;
    private Group group;

    @BeforeEach
    void setUp() {
        studentDao = new StudentDao(entityManager);
        group = entityManager.find(Student.class, 1000L).getGroup();
    }

    @Test
    void shouldCreateNewStudent() {
        Student actual = studentDao.save(new Student("John", "Jackson", "Jackson", 1, group));
        Student expected = new Student(actual.getId(), "John", "Jackson", "Jackson", 1, group);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateStudent() {
        Student student = new Student(1000L, "Mike", "Jackson", "Jackson", 1, group);

        assertNotEquals(student, entityManager.find(Student.class, student.getId()));

        Student actual = studentDao.save(student);

        assertEquals(student, actual);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Student actual = studentDao.getById(1000L).get();
        Student expected = new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, group);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, group),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1,
                        entityManager.find(Student.class, 1001L).getGroup()),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2,
                        entityManager.find(Student.class, 1002L).getGroup()),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2,
                        entityManager.find(Student.class, 1003L).getGroup()),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3,
                        entityManager.find(Student.class, 1004L).getGroup()));
        List<Student> actual = studentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteStudent() {
        assertTrue(studentDao.deleteById(1000L));
        assertFalse(studentDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfStudents() {
        List<Student> audiences = List.of(
                new Student("John", "Jackson", "Jackson", 1, group),
                new Student("Mike", "Conor", "Conor", 2, group));

        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, group),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, entityManager.find(Student.class, 1001L).getGroup()),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, entityManager.find(Student.class, 1002L).getGroup()),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, entityManager.find(Student.class, 1003L).getGroup()),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, entityManager.find(Student.class, 1004L).getGroup()),
                new Student(1L, "John", "Jackson", "Jackson", 1, group),
                new Student(2L, "Mike", "Conor", "Conor", 2, group));
        studentDao.saveAll(audiences);
        List<Student> actual = studentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindStudentNotExist() {
        assertFalse(studentDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfStudentNotExist() {
        assertFalse(() -> studentDao.deleteById(21L));
    }
}
