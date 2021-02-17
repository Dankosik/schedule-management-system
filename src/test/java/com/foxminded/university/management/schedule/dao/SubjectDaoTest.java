package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class SubjectDaoTest extends BaseDaoTest {
    private SubjectDao subjectDao;

    @BeforeEach
    void setUp() {
        subjectDao = new SubjectDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewSubject() {
        Subject subject = new Subject("Art");
        Long subjectId = subjectDao.save(subject).getId();
        assertTrue(testUtils.existsById("subjects", subjectId));

        Map<String, Object> map = testUtils.getEntry("subjects", subjectId);
        Subject actual = new Subject((String) map.get("name"));
        assertEquals(subject, actual);
    }

    @Test
    void shouldUpdateSubject() {
        Subject subject = new Subject(1000L, "Art");
        Long subjectId = subjectDao.save(subject).getId();
        assertTrue(testUtils.existsById("subjects", subjectId));

        Map<String, Object> map = testUtils.getEntry("subjects", subjectId);
        Subject actual = new Subject((Long) map.get("id"), (String) map.get("name"));
        assertEquals(subject, actual);
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("subjects", 1000L);
        Subject expected = new Subject((Long) map.get("id"), (String) map.get("name"));
        Subject actual = subjectDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjects() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math"),
                new Subject(1001L, "Physics"),
                new Subject(1002L, "Programming"));
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
                new Subject("Art"),
                new Subject("Music"));

        List<Subject> expected = List.of(
                new Subject(1000L, "Math"),
                new Subject(1001L, "Physics"),
                new Subject(1002L, "Programming"),
                new Subject(1L, "Art"),
                new Subject(2L, "Music"));
        subjectDao.saveAll(subjects);
        List<Subject> actual = subjectDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSubjectsWithStudentIdOne() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math"),
                new Subject(1002L, "Programming"));
        List<Subject> actual = subjectDao.getSubjectsByStudentId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSubjectsWithTeacherIdOne() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math"),
                new Subject(1001L, "Physics"));
        List<Subject> actual = subjectDao.getSubjectsByTeacherId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindSubjectNotExist() {
        assertFalse(subjectDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfSubjectNotExist() {
        assertFalse(() -> subjectDao.deleteById(21L));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> subjectDao.save(new Subject("Math")));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> subjectDao.save(new Subject(1001L, "Math")));
    }
}
