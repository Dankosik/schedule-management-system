package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
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
class FacultyDaoTest extends BaseDaoTest {
    private FacultyDao facultyDao;
    @Autowired
    private EntityManager entityManager;
    private List<Group> groups;
    private List<Teacher> teachers;

    @BeforeEach
    void setUp() {
        facultyDao = new FacultyDao(entityManager);
        groups = entityManager.find(Faculty.class, 1000L).getGroups();
        teachers = entityManager.find(Faculty.class, 1000L).getTeachers();
    }

    @Test
    void shouldCreateNewFaculty() {
        Faculty actual = facultyDao.save(new Faculty("QWPS", groups, teachers));
        Faculty expected = new Faculty(actual.getId(), "QWPS", groups, teachers);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateFaculty() {
        Faculty faculty = new Faculty(1000L, "QWPS", groups, teachers);

        assertNotEquals(faculty, entityManager.find(Faculty.class, faculty.getId()));

        Faculty actual = facultyDao.save(faculty);

        assertEquals(faculty, actual);
    }

    @Test
    void shouldReturnFacultyWithIdOne() {
        Faculty actual = facultyDao.getById(1000L).get();
        Faculty expected = new Faculty(1000L, "FAIT", groups, teachers);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfFaculties() {
        List<Faculty> expected = List.of(
                new Faculty(1000L, "FAIT", groups, teachers),
                new Faculty(1001L, "FKFN", entityManager.find(Faculty.class, 1001L).getGroups(),
                        entityManager.find(Faculty.class, 1001L).getTeachers()));
        List<Faculty> actual = facultyDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteFaculty() {
        assertTrue(facultyDao.deleteById(1000L));
        assertFalse(facultyDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfFaculties() {
        List<Faculty> faculties = List.of(
                new Faculty("ABCD", null, null),
                new Faculty("IFGH", null, null));

        List<Faculty> expected = List.of(
                new Faculty(1000L, "FAIT", groups, teachers),
                new Faculty(1001L, "FKFN", entityManager.find(Faculty.class, 1001L).getGroups(),
                        entityManager.find(Faculty.class, 1001L).getTeachers()),
                new Faculty(1L, "ABCD", null, null),
                new Faculty(2L, "IFGH", null, null));

        facultyDao.saveAll(faculties);
        List<Faculty> actual = facultyDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindFacultyNotExist() {
        assertFalse(facultyDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfFacultyNotExist() {
        assertFalse(() -> facultyDao.deleteById(21L));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> facultyDao.save(new Faculty("FAIT", groups, teachers)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> facultyDao.save(new Faculty(1000L, "FKFN", groups, teachers)));
    }
}
