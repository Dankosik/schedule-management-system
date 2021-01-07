package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
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
class FacultyDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private FacultyDao facultyDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        facultyDao = new FacultyDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewFaculty() {
        Faculty faculty = new Faculty("QWPS", 1000L);
        Long facultyId = facultyDao.save(faculty).getId();
        assertTrue(testUtils.existsById("faculties", facultyId));

        Map<String, Object> map = testUtils.getEntry("faculties", facultyId);
        Faculty actual = new Faculty((String) map.get("name"), (Long) map.get("university_id"));
        assertEquals(faculty, actual);
    }

    @Test
    void shouldUpdateFaculty() {
        Faculty faculty = new Faculty(1000L, "QWPS", 1000L);
        Long facultyId = facultyDao.save(faculty).getId();
        assertTrue(testUtils.existsById("faculties", facultyId));

        Map<String, Object> map = testUtils.getEntry("faculties", facultyId);
        Faculty actual = new Faculty((Long) map.get("id"), (String) map.get("name"), (Long) map.get("university_id"));
        assertEquals(faculty, actual);
    }

    @Test
    void shouldReturnFacultyWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("faculties", 1000L);
        Faculty expected = new Faculty((Long) map.get("id"), (String) map.get("name"), (Long) map.get("university_id"));
        Faculty actual = facultyDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfFaculties() {
        List<Faculty> expected = List.of(
                new Faculty(1000L, "FAIT", 1000L),
                new Faculty(1001L, "FKFN", 1000L));
        List<Faculty> actual = facultyDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteFaculty() {
        assertTrue(facultyDao.deleteById(1000L));
        assertFalse(testUtils.existsById("faculties", 1000L));
    }

    @Test
    void shouldSaveListOfFaculties() {
        List<Faculty> faculties = List.of(
                new Faculty("ABCD", 1000L),
                new Faculty("IFGH", 1000L));

        List<Faculty> expected = List.of(
                new Faculty(1L, "ABCD", 1000L),
                new Faculty(2L, "IFGH", 1000L));

        facultyDao.saveAll(faculties);
        List<Faculty> actual = facultyDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfFacultiesWithUniversityIdOne() {
        List<Faculty> expected = List.of(
                new Faculty(1000L, "FAIT", 1000L),
                new Faculty(1001L, "FKFN", 1000L));
        List<Faculty> actual = facultyDao.getFacultiesByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfFacultyNotExist() {
        assertThrows(NoSuchElementException.class, () -> facultyDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfFacultyNotExist() {
        assertFalse(() -> facultyDao.deleteById(21L));
    }
}