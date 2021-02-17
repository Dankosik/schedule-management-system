package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class StudentDaoTest extends BaseDaoTest {
    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        studentDao = new StudentDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewStudent() {
        Student student = new Student("John", "Jackson", "Jackson", 1, 1000L);
        Long studentId = studentDao.save(student).getId();
        assertTrue(testUtils.existsById("students", studentId));

        Map<String, Object> map = testUtils.getEntry("students", studentId);
        Student actual = new Student((String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Integer) map.get("course_number"), (Long) map.get("group_id"));
        assertEquals(student, actual);
    }

    @Test
    void shouldUpdateStudent() {
        Student student = new Student(1000L, "John", "Jackson", "Jackson", 1, 1000L);
        Long studentId = studentDao.save(student).getId();
        assertTrue(testUtils.existsById("students", studentId));

        Map<String, Object> map = testUtils.getEntry("students", studentId);
        Student actual = new Student((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Integer) map.get("course_number"), (Long) map.get("group_id"));
        assertEquals(student, actual);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("students", 1000L);
        Student expected = new Student((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Integer) map.get("course_number"), (Long) map.get("group_id"));
        Student actual = studentDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, 1001L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, 1001L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L));
        List<Student> actual = studentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteStudent() {
        assertTrue(studentDao.deleteById(1000L));
        assertFalse(testUtils.existsById("students", 1000L));
    }

    @Test
    void shouldSaveListOfStudents() {
        List<Student> audiences = List.of(
                new Student("John", "Jackson", "Jackson", 1, 1001L),
                new Student("Mike", "Conor", "Conor", 2, 1001L));

        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, 1001L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, 1001L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L),
                new Student(1L, "John", "Jackson", "Jackson", 1, 1001L),
                new Student(2L, "Mike", "Conor", "Conor", 2, 1001L));
        studentDao.saveAll(audiences);
        List<Student> actual = studentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfStudentsWithGroupIdOne() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L));
        List<Student> actual = studentDao.getStudentsByGroupId(1000L);

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
