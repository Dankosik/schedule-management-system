package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Department;
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
class DepartmentDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");
    private DepartmentDao departmentDao;
    private TestUtils testUtils;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        departmentDao = new DepartmentDao(dataSource);
        testUtils = new TestUtils(dataSource);
    }

    @Test
    void shouldCreateNewDepartment() {
        Department department = new Department("ABC", 1000L, 1000L);
        Long departmentId = departmentDao.save(department).getId();
        assertTrue(testUtils.existsById("departments", departmentId));

        Map<String, Object> map = testUtils.getEntry("departments", departmentId);
        Department actual = new Department((String) map.get("name"), (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(department, actual);
    }

    @Test
    void shouldUpdateDepartment() {
        Department department = new Department(1000L, "ABC", 1000L, 1000L);
        Long departmentId = departmentDao.save(department).getId();
        assertTrue(testUtils.existsById("departments", departmentId));

        Map<String, Object> map = testUtils.getEntry("departments", departmentId);
        Department actual = new Department((Long) map.get("id"), (String) map.get("name"), (Long) map.get("faculty_id"),
                (Long) map.get("university_id"));
        assertEquals(department, actual);
    }

    @Test
    void shouldReturnDepartmentWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("departments", 1000L);
        Department expected = new Department((Long) map.get("id"), (String) map.get("name"), (Long) map.get("faculty_id"),
                (Long) map.get("university_id"));
        Department actual = departmentDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnLIstOfDepartments() {
        List<Department> expected = List.of(
                new Department(1000L, "Department of Automation and System Engineering", 1000L, 1000L),
                new Department(1001L, "Department of Higher Mathematics", 1001L, 1000L));
        List<Department> actual = departmentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteDepartment() {
        assertTrue(departmentDao.deleteById(1000L));
        assertFalse(testUtils.existsById("departments", 1000L));
    }

    @Test
    void shouldSaveListOfDepartments() {
        List<Department> departments = List.of(
                new Department("Department of Computer Science", 1000L, 1000L),
                new Department("Department of Informatics", 1001L, 1000L));
        List<Department> expected = List.of(
                new Department(1L, "Department of Computer Science", 1000L, 1000L),
                new Department(2L, "Department of Informatics", 1001L, 1000L));
        departmentDao.saveAll(departments);
        List<Department> actual = departmentDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfDepartmentsWithUniversityIdOne() {
        List<Department> expected = List.of(
                new Department(1000L, "Department of Automation and System Engineering", 1000L, 1000L),
                new Department(1001L, "Department of Higher Mathematics", 1001L, 1000L));
        List<Department> actual = departmentDao.getDepartmentsByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfDepartmentsWithFacultyIdOne() {
        List<Department> expected = List.of(
                new Department(1000L, "Department of Automation and System Engineering", 1000L, 1000L));
        List<Department> actual = departmentDao.getDepartmentsByFacultyId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldThrowExceptionIfDepartmentNotExist() {
        assertThrows(NoSuchElementException.class, () -> departmentDao.getById(21L).get());
    }

    @Test
    void shouldReturnFalseIfDepartmentNotExist() {
        assertFalse(() -> departmentDao.deleteById(21L));
    }
}