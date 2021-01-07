package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.TestUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class StudentDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private StudentDao studentDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        studentDao = new StudentDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewStudent() {
        Student student = new Student("John", "Jackson", "Jackson", 1, 1001L, 1000L, 1000L);
        Long studentId = studentDao.save(student).getId();
        assertTrue(testUtils.existsById("students", studentId));

        Map<String, Object> map = testUtils.getEntry("students", studentId);
        Student actual = new Student((String) map.get("first_name"), (String) map.get("last_name"), (String) map.get("middle_name"),
                (Integer) map.get("course_number"), (Long) map.get("group_id"), (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(student, actual);
    }

    @Test
    void shouldUpdateStudent() {
        Student student = new Student(1000L, "John", "Jackson", "Jackson", 1, 1001L, 1000L, 1000L);
        Long studentId = studentDao.save(student).getId();
        assertTrue(testUtils.existsById("students", studentId));

        Map<String, Object> map = testUtils.getEntry("students", studentId);
        Student actual = new Student((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Integer) map.get("course_number"), (Long) map.get("group_id"),
                (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(student, actual);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("students", 1000L);
        Student expected = new Student((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Integer) map.get("course_number"), (Long) map.get("group_id"),
                (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        Student actual = studentDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L, 1000L, 1000L),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, 1001L, 1000L, 1000L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L, 1001L, 1000L),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, 1001L, 1001L, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L, 1000L, 1000L));
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
                new Student("John", "Jackson", "Jackson", 1, 1001L, 1000L, 1000L),
                new Student("Mike", "Conor", "Conor", 2, 1001L, 1000L, 1000L));

        List<Student> expected = List.of(
                new Student(1L, "John", "Jackson", "Jackson", 1, 1001L, 1000L, 1000L),
                new Student(2L, "Mike", "Conor", "Conor", 2, 1001L, 1000L, 1000L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L, 1001L, 1000L),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, 1001L, 1001L, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L, 1000L, 1000L));
        studentDao.saveAll(audiences);
        List<Student> actual = studentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfStudentsWithUniversityIdOne() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L, 1000L, 1000L),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, 1001L, 1000L, 1000L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L, 1001L, 1000L),
                new Student(1003L, "Jessa", "Costin", "Heeron", 2, 1001L, 1001L, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L, 1000L, 1000L));
        List<Student> actual = studentDao.getStudentsByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfStudentsWithGroupIdOne() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L, 1000L, 1000L),
                new Student(1002L, "Minetta", "Funcheon", "Sayle", 2, 1000L, 1001L, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L, 1000L, 1000L));
        List<Student> actual = studentDao.getStudentsByGroupId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfStudentsWithFacultyIdOne() {
        List<Student> expected = List.of(
                new Student(1000L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1000L, 1000L, 1000L),
                new Student(1001L, "Lindsey", "Syplus", "Slocket", 1, 1001L, 1000L, 1000L),
                new Student(1004L, "Earl", "Djekic", "Tremble", 3, 1000L, 1000L, 1000L));
        List<Student> actual = studentDao.getStudentsByFacultyId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfStudentNotExist() {
        assertThrows(NoSuchElementException.class, () -> studentDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfStudentNotExist() {
        assertFalse(() -> studentDao.deleteById(21L));
    }
}