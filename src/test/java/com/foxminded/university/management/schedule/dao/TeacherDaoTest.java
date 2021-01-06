package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class TeacherDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private TeacherDao teacherDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        teacherDao = new TeacherDao(dataSource);
    }

    @Test
    void shouldCreateNewTeacher() {
        Teacher teacher = new Teacher("John", "Jackson", "Jackson", 1L, 1L);
        teacherDao.delete(teacherDao.getById(1L).get());
        teacherDao.save(teacher);
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L);

        assertEquals(expected, teacherDao.getById(1L).get());
    }

    @Test
    void shouldUpdateTeacher() {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L);
        assertNotEquals(teacher, teacherDao.getById(1L).get());
        teacherDao.save(teacher);

        assertEquals(teacher, teacherDao.getById(1L).get());
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        Teacher expected = new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L);
        Teacher actual = teacherDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfStudents() {
        List<Teacher> expected = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L),
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey", 2L, 1L));
        List<Teacher> actual = teacherDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteStudent() {
        Teacher teacher = new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L);
        List<Teacher> expected = List.of(
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey", 2L, 1L));
        assertTrue(teacherDao.delete(teacher));
        List<Teacher> actual = teacherDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfStudents() {
        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", 1L, 1L),
                new Teacher("Mike", "Conor", "Conor", 2L, 1L));

        List<Teacher> expected = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L));
        teacherDao.delete(new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L));
        teacherDao.delete(new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey", 2L, 1L));
        teacherDao.saveAll(teachers);

        assertEquals(expected, teacherDao.getAll());
    }

    @Test
    void shouldReturnListOfTeachersWithUniversityIdOne() {
        List<Teacher> expected = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L),
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey", 2L, 1L));
        List<Teacher> actual = teacherDao.getTeachersByUniversityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfTeachersWithStudentIdOne() {
        List<Teacher> expected = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L));
        List<Teacher> actual = teacherDao.getTeachersByStudentId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfTeacherNotExist() {
        assertThrows(NoSuchElementException.class, () -> teacherDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfTeacherNotExist() {
        assertFalse(() -> teacherDao.delete(new Teacher(21L, "Hillel", "St. Leger", "Lugard", 1L, 1L)));
    }
}