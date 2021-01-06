package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
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
class GroupDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");

    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        groupDao = new GroupDao(dataSource);
    }

    @Test
    void shouldCreateNewGroup() {
        Group group = new Group("AB-81", 1L, 1L, 2L, 1L);
        groupDao.delete(groupDao.getById(1L).get());
        groupDao.save(group);
        Group expected = new Group(1L, "AB-81", 1L, 1L, 2L, 1L);

        assertEquals(expected, groupDao.getById(1L).get());
    }

    @Test
    void shouldUpdateGroup() {
        Group group = new Group(1L, "AB-81", 1L, 1L, 2L, 1L);
        assertNotEquals(group, groupDao.getById(1L).get());
        groupDao.save(group);

        assertEquals(group, groupDao.getById(1L).get());
    }

    @Test
    void shouldReturnGroupWithIdOne() {
        Group expected = new Group(1L, "AB-91", 1L, 1L, 1L, 1L);
        Group actual = groupDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroups() {
        List<Group> expected = List.of(
                new Group(1L, "AB-91", 1L, 1L, 1L, 1L),
                new Group(2L, "BC-01", 2L, 2L, 2L, 1L));
        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteGroup() {
        Group group = new Group(1L, "AB-91", 1L, 1L, 1L, 1L);
        List<Group> expected = List.of(new Group(2L, "BC-01", 2L, 2L, 2L, 1L));
        assertTrue(groupDao.delete(group));
        List<Group> actual = groupDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfGroups() {
        List<Group> audiences = List.of(
                new Group("CD-71", 1L, 1L, 2L, 1L),
                new Group("IF-61", 2L, 2L, 2L, 1L));

        List<Group> expected = List.of(
                new Group(1L, "CD-71", 1L, 1L, 2L, 1L),
                new Group(2L, "IF-61", 2L, 2L, 2L, 1L));
        groupDao.delete(new Group(1L, "AB-91", 1L, 1L, 1L, 1L));
        groupDao.delete(new Group(2L, "BC-01", 2L, 2L, 2L, 1L));
        groupDao.saveAll(audiences);

        assertEquals(expected, groupDao.getAll());
    }

    @Test
    void shouldReturnListOfGroupsWithUniversityIdOne() {
        List<Group> expected = List.of(
                new Group(1L, "AB-91", 1L, 1L, 1L, 1L),
                new Group(2L, "BC-01", 2L, 2L, 2L, 1L));
        List<Group> actual = groupDao.getGroupsByUniversityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroupsWithFacultyIdOne() {
        List<Group> expected = List.of(
                new Group(1L, "AB-91", 1L, 1L, 1L, 1L));
        List<Group> actual = groupDao.getGroupsByFacultyId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroupsWithDepartmentIdTwo() {
        List<Group> expected = List.of(
                new Group(2L, "BC-01", 2L, 2L, 2L, 1L));
        List<Group> actual = groupDao.getGroupsByDepartmentId(2L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroupsWithLectureIdOne() {
        List<Group> expected = List.of(
                new Group(1L, "AB-91", 1L, 1L, 1L, 1L));
        List<Group> actual = groupDao.getGroupsByLectureId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionIfGroupNotExist() {
        assertThrows(NoSuchElementException.class, ()->groupDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfGroupNotExist() {
        assertFalse(()->groupDao.delete(new Group(21L, "AB-91", 1L, 1L, 1L, 1L)));
    }
}