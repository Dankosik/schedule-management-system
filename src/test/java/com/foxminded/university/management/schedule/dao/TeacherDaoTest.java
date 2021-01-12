package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherDaoTest extends BaseDaoTest {
    private TeacherDao teacherDao;

    @BeforeEach
    void setUp() {
        teacherDao = new TeacherDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewTeacher() {
        Teacher teacher = new Teacher("John", "Jackson", "Jackson", 1000L, 1000L);
        Long teacherId = teacherDao.save(teacher).getId();
        assertTrue(testUtils.existsById("teachers", teacherId));

        Map<String, Object> map = testUtils.getEntry("teachers", teacherId);
        Teacher actual = new Teacher((String) map.get("first_name"), (String) map.get("last_name"), (String) map.get("middle_name"),
                (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(teacher, actual);
    }

    @Test
    void shouldUpdateTeacher() {
        Teacher teacher = new Teacher(1000L, "John", "Jackson", "Jackson", 1000L, 1000L);
        Long teacherId = teacherDao.save(teacher).getId();
        assertTrue(testUtils.existsById("teachers", teacherId));

        Map<String, Object> map = testUtils.getEntry("teachers", teacherId);
        Teacher actual = new Teacher((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(teacher, actual);
    }

    @Test
    void shouldReturnTeacherWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("teachers", 1000L);
        Teacher expected = new Teacher((Long) map.get("id"), (String) map.get("first_name"), (String) map.get("last_name"),
                (String) map.get("middle_name"), (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        Teacher actual = teacherDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfTeachers() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey", 1001L, 1000L));
        List<Teacher> actual = teacherDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteTeacher() {
        assertTrue(teacherDao.deleteById(1000L));
        assertFalse(testUtils.existsById("teachers", 1000L));
    }

    @Test
    void shouldSaveListOfTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", 1000L, 1000L),
                new Teacher("Mike", "Conor", "Conor", 1001L, 1000L));

        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey", 1001L, 1000L),
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
    void shouldReturnListOfTeachersWithFacultyIdOne() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", 1000L, 1000L));
        List<Teacher> actual = teacherDao.getTeachersByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindLTeacherNotExist() {
        assertFalse(teacherDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfTeacherNotExist() {
        assertFalse(() -> teacherDao.deleteById(21L));
    }
}
