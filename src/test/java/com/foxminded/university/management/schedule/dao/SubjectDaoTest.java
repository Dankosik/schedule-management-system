package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class SubjectDaoTest extends BaseDaoTest {
    private SubjectDao subjectDao;
    @Autowired
    private EntityManager entityManager;
    private List<Lesson> lessons;

    @BeforeEach
    void setUp() {
        subjectDao = new SubjectDao(entityManager);
        lessons = entityManager.find(Subject.class, 1000L).getLessons();
    }

    @Test
    void shouldCreateNewSubject() {
        Subject actual = subjectDao.save(new Subject("Art", lessons));

        Subject expected = new Subject(actual.getId(), "Art", lessons);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateSubject() {
        Subject subject = new Subject(1000L, "Art", lessons);

        assertNotEquals(subject, entityManager.find(Subject.class, subject.getId()));

        Subject actual = subjectDao.save(subject);

        assertEquals(subject, actual);
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        Subject actual = subjectDao.getById(1000L).get();
        Subject expected = new Subject(1000L, "Math", lessons);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSubjects() {
        List<Subject> expected = List.of(
                new Subject(1000L, "Math", lessons),
                new Subject(1001L, "Physics", entityManager.find(Subject.class, 1001L).getLessons()),
                new Subject(1002L, "Programming", entityManager.find(Subject.class, 1002L).getLessons()));
        List<Subject> actual = subjectDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteSubject() {
        assertTrue(subjectDao.deleteById(1000L));
        assertFalse(subjectDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Subject> subjects = List.of(
                new Subject("Art", null),
                new Subject("Music", null));

        List<Subject> expected = List.of(
                new Subject(1000L, "Math", lessons),
                new Subject(1001L, "Physics", entityManager.find(Subject.class, 1001L).getLessons()),
                new Subject(1002L, "Programming", entityManager.find(Subject.class, 1002L).getLessons()),
                new Subject(1L, "Art", null),
                new Subject(2L, "Music", null));
        subjectDao.saveAll(subjects);
        List<Subject> actual = subjectDao.getAll();

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
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> subjectDao.save(new Subject("Math", lessons)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> subjectDao.save(new Subject(1001L, "Math", lessons)));
    }
}
