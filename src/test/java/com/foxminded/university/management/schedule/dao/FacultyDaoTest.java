package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class FacultyDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private FacultyDao facultyDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        facultyDao = new FacultyDao(dataSource);
    }

    @Test
    void shouldCreateNewFaculty() {
        Faculty faculty = new Faculty("QWPS", 1L);
        facultyDao.delete(facultyDao.getById(1L).get());
        facultyDao.save(faculty);
        Faculty expected = new Faculty(1L, "QWPS", 1L);

        assertEquals(expected, facultyDao.getById(1L).get());
    }

    @Test
    void shouldUpdateFaculty() {
        Faculty faculty = new Faculty(1L, "WQE", 1L);
        assertNotEquals(faculty, facultyDao.getById(1L).get());
        facultyDao.save(faculty);

        assertEquals(faculty, facultyDao.getById(1L).get());
    }

    @Test
    void shouldReturnFacultyWithIdOne() {
        Faculty expected = new Faculty(1L, "FAIT", 1L);
        Faculty actual = facultyDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfFaculties() {
        List<Faculty> expected = List.of(
                new Faculty(1L, "FAIT", 1L),
                new Faculty(2L, "FKFN", 1L));
        List<Faculty> actual = facultyDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteFaculty() {
        Faculty faculty = new Faculty(1L, "FAIT", 1L);
        List<Faculty> expected = List.of(new Faculty(2L, "FKFN", 1L));
        assertTrue(facultyDao.delete(faculty));
        List<Faculty> actual = facultyDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfFaculties() {
        List<Faculty> faculties = List.of(
                new Faculty("ABCD", 1L),
                new Faculty("IFGH", 1L));

        List<Faculty> expected = List.of(
                new Faculty(1L, "ABCD", 1L),
                new Faculty(2L, "IFGH", 1L));

        facultyDao.delete(new Faculty(1L, "FAIT", 1L));
        facultyDao.delete(new Faculty(2L, "FKFN", 1L));
        facultyDao.saveAll(faculties);
        assertEquals(expected, facultyDao.getAll());

    }

    @Test
    void shouldReturnListOfFacultiesWithUniversityIdOne() {
        List<Faculty> expected = List.of(
                new Faculty(1L, "FAIT", 1L),
                new Faculty(2L, "FKFN", 1L));
        List<Faculty> actual = facultyDao.getFacultiesByUniversityId(1L);

        assertEquals(expected, actual);
    }

}