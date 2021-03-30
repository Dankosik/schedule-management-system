package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class TeacherDaoTest extends BaseDaoTest {
    private TeacherDao teacherDao;
    @Autowired
    private EntityManager entityManager;
    private Faculty faculty;
    private List<Lecture> lectures;

    @BeforeEach
    void setUp() {
        teacherDao = new TeacherDao(entityManager);
        faculty = entityManager.find(Teacher.class, 1000L).getFaculty();
        lectures = entityManager.find(Teacher.class, 1000L).getLectures();
    }

    @Test
    void shouldCreateNewTeacher() {
        Teacher actual = teacherDao.save(new Teacher("John", "Jackson", "Jackson", faculty, lectures));
        Teacher expected = new Teacher(actual.getId(), "John", "Jackson", "Jackson", faculty, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateTeacher() {
        Teacher teacher = new Teacher(1000L, "John", "Jackson", "Jackson", faculty, lectures);

        assertNotEquals(teacher, entityManager.find(Teacher.class, teacher.getId()));

        Teacher actual = teacherDao.save(teacher);

        assertEquals(teacher, actual);
    }

    @Test
    void shouldReturnTeacherWithIdOne() {
        Teacher actual = teacherDao.getById(1000L).get();
        Teacher expected = new Teacher(1000L, "Hillel", "St. Leger", "Lugard", faculty, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfTeachers() {
        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", faculty, lectures),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey",
                        entityManager.find(Teacher.class, 1001L).getFaculty(),
                        entityManager.find(Teacher.class, 1001L).getLectures()));
        List<Teacher> actual = teacherDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteTeacher() {
        assertTrue(teacherDao.deleteById(1000L));
        assertFalse(teacherDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", faculty, null),
                new Teacher("Mike", "Conor", "Conor", faculty, null));

        List<Teacher> expected = List.of(
                new Teacher(1000L, "Hillel", "St. Leger", "Lugard", faculty, lectures),
                new Teacher(1001L, "Lynsey", "Grzeszczak", "McPhillimey",
                        entityManager.find(Teacher.class, 1001L).getFaculty(),
                        entityManager.find(Teacher.class, 1001L).getLectures()),
                new Teacher(1L, "John", "Jackson", "Jackson", faculty, null),
                new Teacher(2L, "Mike", "Conor", "Conor", faculty, null));
        teacherDao.saveAll(teachers);
        List<Teacher> actual = teacherDao.getAll();

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
