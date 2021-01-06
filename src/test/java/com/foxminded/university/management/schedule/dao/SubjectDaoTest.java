package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class SubjectDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private SubjectDao subjectDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        subjectDao = new SubjectDao(dataSource);
    }

    @Test
    void shouldCreateNewSubject() {
        Subject subject = new Subject("Art", 1L);
        subjectDao.delete(subjectDao.getById(1L).get());
        subjectDao.save(subject);
        Subject expected = new Subject(1L, "Art", 1L);

        assertEquals(expected, subjectDao.getById(1L).get());
    }

    @Test
    void shouldUpdateSubject() {
        Subject subject = new Subject(1L, "Art", 1L);
        assertNotEquals(subject, subjectDao.getById(1L).get());
        subjectDao.save(subject);

        assertEquals(subject, subjectDao.getById(1L).get());
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        Subject expected = new Subject(1L, "Math", 1L);
        Subject actual = subjectDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjects() {
        List<Subject> expected = List.of(
                new Subject(1L, "Math", 1L),
                new Subject(2L, "Physics", 1L),
                new Subject(3L, "Programming", 1L));
        List<Subject> actual = subjectDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteSubject() {
        Subject subject = new Subject(1L, "Math", 1L);
        List<Subject> expected = List.of(
                new Subject(2L, "Physics", 1L),
                new Subject(3L, "Programming", 1L));
        assertTrue(subjectDao.delete(subject));
        List<Subject> actual = subjectDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Subject> subjects = List.of(
                new Subject("Art", 1L),
                new Subject("Music", 1L));

        List<Subject> expected = List.of(
                new Subject(3L, "Programming", 1L),
                new Subject(1L, "Art", 1L),
                new Subject(2L, "Music", 1L));
        subjectDao.delete(new Subject(1L, "Math", 1L));
        subjectDao.delete(new Subject(2L, "Physics", 1L));
        subjectDao.saveAll(subjects);

        assertEquals(expected, subjectDao.getAll());
    }

    @Test
    void shouldReturnListOfSubjectsWithUniversityIdOne() {
        List<Subject> expected = List.of(
                new Subject(1L, "Math", 1L),
                new Subject(2L, "Physics", 1L),
                new Subject(3L, "Programming", 1L));
        List<Subject> actual = subjectDao.getSubjectsByUniversityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjectsWithStudentIdOne() {
        List<Subject> expected = List.of(
                new Subject(1L, "Math", 1L),
                new Subject(3L, "Programming", 1L));
        List<Subject> actual = subjectDao.getSubjectsByStudentId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjectsWithTeacherIdOne() {
        List<Subject> expected = List.of(
                new Subject(1L, "Math", 1L),
                new Subject(2L, "Physics", 1L));
        List<Subject> actual = subjectDao.getSubjectsByTeacherId(1L);

        assertEquals(expected, actual);
    }
}