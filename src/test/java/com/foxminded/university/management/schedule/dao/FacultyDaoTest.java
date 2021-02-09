package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
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
class FacultyDaoTest extends BaseDaoTest {
    private FacultyDao facultyDao;

    @BeforeEach
    void setUp() {
        facultyDao = new FacultyDao(jdbcTemplate);
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
                new Faculty(1000L, "FAIT", 1000L),
                new Faculty(1001L, "FKFN", 1000L),
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
    void shouldNotFindFacultyNotExist() {
        assertFalse(facultyDao.getById(21L).isPresent());
    }


    @Test
    void shouldReturnFalseIfFacultyNotExist() {
        assertFalse(() -> facultyDao.deleteById(21L));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> facultyDao.save(new Faculty("FAIT", 1000L)));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCUpdate() {
        assertThrows(DuplicateKeyException.class, () -> facultyDao.save(new Faculty(1000L, "FKFN", 1000L)));
    }
}
