package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class StudentDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private StudentDao studentDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        studentDao = new StudentDao(dataSource);
    }

    @Test
    void shouldCreateNewStudent() {
        Student student = new Student("John", "Jackson", "Jackson", 1, 2L, 1L, 1L);
        studentDao.delete(studentDao.getById(1L).get());
        studentDao.save(student);
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 2L, 1L, 1L);

        assertEquals(expected, studentDao.getById(1L).get());
    }

    @Test
    void shouldUpdateStudent() {
        Student student = new Student(1L, "John", "Jackson", "Jackson", 1, 2L, 1L, 1L);
        assertNotEquals(student, studentDao.getById(1L).get());
        studentDao.save(student);

        assertEquals(student, studentDao.getById(1L).get());
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Student expected = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L);
        Student actual = studentDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Student> expected = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1L, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 1L, 2L, 1L),
                new Student(4L, "Jessa", "Costin", "Heeron", 2, 2L, 2L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L));
        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteStudent() {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L);
        List<Student> expected = List.of(
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1L, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 1L, 2L, 1L),
                new Student(4L, "Jessa", "Costin", "Heeron", 2, 2L, 2L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L));
        assertTrue(studentDao.delete(student));
        List<Student> actual = studentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfStudents() {
        List<Student> audiences = List.of(
                new Student("John", "Jackson", "Jackson", 1, 2L, 1L, 1L),
                new Student("Mike", "Conor", "Conor", 2, 2L, 1L, 1L));

        List<Student> expected = List.of(
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 1L, 2L, 1L),
                new Student(4L, "Jessa", "Costin", "Heeron", 2, 2L, 2L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L),
                new Student(1L, "John", "Jackson", "Jackson", 1, 2L, 1L, 1L),
                new Student(2L, "Mike", "Conor", "Conor", 2, 2L, 1L, 1L));
        studentDao.delete(new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L));
        studentDao.delete(new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1L, 1L));
        studentDao.saveAll(audiences);

        assertEquals(expected, studentDao.getAll());
    }

    @Test
    void shouldReturnListOfStudentsWithUniversityIdOne() {
        List<Student> expected = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1L, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 1L, 2L, 1L),
                new Student(4L, "Jessa", "Costin", "Heeron", 2, 2L, 2L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L));
        List<Student> actual = studentDao.getStudentsByUniversityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudentsWithGroupIdOne() {
        List<Student> expected = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 1L, 2L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L));
        List<Student> actual = studentDao.getStudentsByGroupId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudentsWithFacultyIdOne() {
        List<Student> expected = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1L, 1L),
                new Student(5L, "Earl", "Djekic", "Tremble", 3, 1L, 1L, 1L));
        List<Student> actual = studentDao.getStudentsByFacultyId(1L);

        assertEquals(expected, actual);
    }
}