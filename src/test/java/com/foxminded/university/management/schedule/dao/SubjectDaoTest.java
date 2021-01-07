package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Subject;
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
class SubjectDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private SubjectDao subjectDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        subjectDao = new SubjectDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewSubject() {
        Subject subject = new Subject("Art", 1000L);
        Long subjectId = subjectDao.save(subject).getId();
        assertTrue(testUtils.existsById("subjects", subjectId));

        Map<String, Object> map = testUtils.getEntry("subjects", subjectId);
        Subject actual = new Subject((String) map.get("name"), (Long) map.get("university_id"));
        assertEquals(subject, actual);
    }

    @Test
    void shouldUpdateSubject() {
        Subject subject = new Subject(1000L, "Art", 1000L);
        Long subjectId = subjectDao.save(subject).getId();
        assertTrue(testUtils.existsById("subjects", subjectId));

        Map<String, Object> map = testUtils.getEntry("subjects", subjectId);
        Subject actual = new Subject((Long) map.get("id"), (String) map.get("name"), (Long) map.get("university_id"));
        assertEquals(subject, actual);
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("subjects", 1000L);
        Subject expected = new Subject((Long) map.get("id"), (String) map.get("name"), (Long) map.get("university_id"));
        Subject actual = subjectDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjects() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math", 1000L),
                new Subject(1001L, "Physics", 1000L),
                new Subject(1002L, "Programming", 1000L));
        List<Subject> actual = subjectDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteSubject() {
        assertTrue(subjectDao.deleteById(1000L));
        assertFalse(testUtils.existsById("subjects", 1000L));
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Subject> subjects = List.of(
                new Subject("Art", 1000L),
                new Subject("Music", 1000L));

        List<Subject> expected = List.of(
                new Subject(1L, "Art", 1000L),
                new Subject(2L, "Music", 1000L),
                new Subject(1002L, "Programming", 1000L));
        subjectDao.saveAll(subjects);
        List<Subject> actual = subjectDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSubjectsWithUniversityIdOne() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math", 1000L),
                new Subject(1001L, "Physics", 1000L),
                new Subject(1002L, "Programming", 1000L));
        List<Subject> actual = subjectDao.getSubjectsByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSubjectsWithStudentIdOne() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math", 1000L),
                new Subject(1002L, "Programming", 1000L));
        List<Subject> actual = subjectDao.getSubjectsByStudentId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSubjectsWithTeacherIdOne() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math", 1000L),
                new Subject(1001L, "Physics", 1000L));
        List<Subject> actual = subjectDao.getSubjectsByTeacherId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfSubjectNotExist() {
        assertThrows(NoSuchElementException.class, () -> subjectDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfSubjectNotExist() {
        assertFalse(() -> subjectDao.deleteById(21L));
    }
}