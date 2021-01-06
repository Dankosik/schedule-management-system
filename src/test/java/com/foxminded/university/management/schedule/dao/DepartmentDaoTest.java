package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class DepartmentDaoTest {
    @Container
    private final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:12")
                    .withInitScript("init_test_db.sql");
    private DepartmentDao departmentDao;

    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(POSTGRESQL_CONTAINER.getJdbcUrl());
        dataSource.setUsername(POSTGRESQL_CONTAINER.getUsername());
        dataSource.setPassword(POSTGRESQL_CONTAINER.getPassword());
        departmentDao = new DepartmentDao(dataSource);
    }

    @Test
    void shouldCreateNewDepartment() {
        Department department = new Department("ABC", 1L, 1L);
        departmentDao.delete(departmentDao.getById(1L).get());
        departmentDao.save(department);
        Department expected = new Department(1L, "ABC", 1L, 1L);

        assertEquals(expected, departmentDao.getById(1L).get());
    }

    @Test
    void shouldUpdateDepartment() {
        Department department = new Department(1L, "ABCD", 2L, 1L);
        assertNotEquals(department, departmentDao.getById(1L).get());
        departmentDao.save(department);

        assertEquals(department, departmentDao.getById(1L).get());
    }

    @Test
    void shouldReturnDepartmentWithIdOne() {
        Department expected = new Department(1L, "Department of Automation and System Engineering", 1L, 1L);
        Department actual = departmentDao.getById(1L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnLIstOfDepartments() {
        List<Department> expected = List.of(
                new Department(1L, "Department of Automation and System Engineering", 1L, 1L),
                new Department(2L, "Department of Higher Mathematics", 2L, 1L));
        List<Department> actual = departmentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteDepartment() {
        Department department = new Department(1L, "Department of Automation and System Engineering", 1L, 1L);
        List<Department> expected = List.of(new Department(2L, "Department of Higher Mathematics", 2L, 1L));
        assertTrue(departmentDao.delete(department));
        List<Department> actual = departmentDao.getAll();

        assertEquals(expected, actual);
    }

    @Test
    void shouldSaveListOfDepartments() {
        List<Department> departments = List.of(
                new Department("Department of Computer Science", 1L, 1L),
                new Department("Department of Informatics", 2L, 1L));
        List<Department> expected = List.of(
                new Department(1L, "Department of Computer Science", 1L, 1L),
                new Department(2L, "Department of Informatics", 2L, 1L));
        departmentDao.delete(new Department(1L, "Department of Automation and System Engineering", 1L, 1L));
        departmentDao.delete(new Department(2L, "Department of Higher Mathematics", 2L, 1L));
        departmentDao.saveAll(departments);
        assertEquals(expected, departmentDao.getAll());
    }

    @Test
    void shouldReturnListOfDepartmentsWithUniversityIdOne() {
        List<Department> expected = List.of(
                new Department(1L, "Department of Automation and System Engineering", 1L, 1L),
                new Department(2L, "Department of Higher Mathematics", 2L, 1L));
        List<Department> actual = departmentDao.getDepartmentsByUniversityId(1L);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfDepartmentsWithFacultyIdOne() {
        List<Department> expected = List.of(
                new Department(1L, "Department of Automation and System Engineering", 1L, 1L));
        List<Department> actual = departmentDao.getDepartmentsByFacultyId(1L);

        assertEquals(expected, actual);
    }
}