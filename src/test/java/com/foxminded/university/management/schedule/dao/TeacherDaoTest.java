package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Teacher;
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
class TeacherDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private TeacherDao teacherDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        teacherDao = new TeacherDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewTeacher() {
        Teacher teacher = new Teacher("John", "Jackson", "Jackson", 1000L, 1000L);
        Long teacherId = teacherDao.save(teacher).getId();
        assertTrue(testUtils.existsById("teachers", teacherId));

        Map<String, Object> map = testUtils.getEntry("teachers", teacherId);
        Teacher actual = new Teacher((String) map.get("first_name"), (String) map.get("last_name"), (String) map.get("middle_name"),
                (Long) map.get("student_id"), (Long) map.get("university_id"));
        assertEquals(teacher, actual);
    }

    @Test
    void shouldUpdateTeacher() {
        Teacher teacher = new Teacher(1000L, "John", "Jackson", "Jackson", 1000L, 1000L);
        Long teacherId = teacherDao.save(teacher).getId();
        assertTrue(testUtils.existsById("teachers", teacherId));

        Map<String, Object> map = testUtils.getEntry("teachers", teacherId);
        Teacher actual = new Teacher((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"), (String) map.get("middle_name"),
                (Long) map.get("student_id"), (Long) map.get("university_id"));
        assertEquals(teacher, actual);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("teachers", 1000L);
        Teacher expected = new Teacher((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"), (String) map.get("middle_name"),
                (Long) map.get("student_id"), (Long) map.get("university_id"));
        Teacher actual = teacherDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey", 1001L, 1000L));
        List<Teacher> actual = teacherDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteStudent() {
        assertTrue(teacherDao.deleteById(1000L));
        assertFalse(testUtils.existsById("teachers", 1000L));
    }

    @Test
    void shouldSaveListOfStudents() {
        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", 1000L, 1000L),
                new Teacher("Mike", "Conor", "Conor", 1001L, 1000L));

        List<Teacher> expected = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1000L, 1000L),
                new Teacher(2L, "Mike", "Conor", "Conor", 1001L, 1000L));
        teacherDao.saveAll(teachers);
        List<Teacher> actual = teacherDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfTeachersWithUniversityIdOne() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey", 1001L, 1000L));
        List<Teacher> actual = teacherDao.getTeachersByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfTeachersWithStudentIdOne() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L));
        List<Teacher> actual = teacherDao.getTeachersByStudentId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfTeacherNotExist() {
        assertThrows(NoSuchElementException.class, () -> teacherDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfTeacherNotExist() {
        assertFalse(() -> teacherDao.deleteById(21L));
    }
}