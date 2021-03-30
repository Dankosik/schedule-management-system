package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
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
class GroupDaoTest extends BaseDaoTest {
    private GroupDao groupDao;
    @Autowired
    private EntityManager entityManager;
    private Faculty faculty;
    private List<Student> students;
    private List<Lecture> lectures;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDao(entityManager);
        faculty = entityManager.find(Group.class, 1000L).getFaculty();
        students = entityManager.find(Group.class, 1000L).getStudents();
        lectures = entityManager.find(Group.class, 1000L).getLectures();
    }

    @Test
    void shouldCreateNewGroup() {
        Group actual = groupDao.save(new Group("AB-11", faculty, students, lectures));
        Group expected = new Group(actual.getId(), "AB-11", faculty, students, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateGroup() {
        Group group = new Group(1000L, "AB-81", faculty, students, lectures);

        assertNotEquals(group, entityManager.find(Group.class, group.getId()));

        Group actual = groupDao.save(group);

        assertEquals(group, actual);
    }

    @Test
    void shouldReturnGroupWithIdOne() {
        Group actual = groupDao.getById(1000L).get();
        Group expected = new Group(1000L, "AB-91", faculty, students, lectures);
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroups() {
        List<Group> expected = List.of(
                new Group(1000L, "AB-91", faculty, students, lectures),
                new Group(1001L, "BC-01", entityManager.find(Group.class, 1001L).getFaculty(),
                        entityManager.find(Group.class, 1001L).getStudents(), entityManager.find(Group.class, 1001L).getLectures()));
        List<Group> actual = groupDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteGroup() {
        assertTrue(groupDao.deleteById(1000L));
        assertFalse(groupDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfGroups() {
        List<Group> groups = List.of(
                new Group("CD-71", faculty, null, null),
                new Group("IF-61", faculty, null, null));

        List<Group> expected = List.of(
                new Group(1000L, "AB-91", faculty, students, lectures),
                new Group(1001L, "BC-01", entityManager.find(Group.class, 1001L).getFaculty(),
                        entityManager.find(Group.class, 1001L).getStudents(), entityManager.find(Group.class, 1001L).getLectures()),
                new Group(1L, "CD-71", faculty, null, null),
                new Group(2L, "IF-61", faculty, null, null));
        groupDao.saveAll(groups);
        List<Group> actual = groupDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindGroupNotExist() {
        assertFalse(groupDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfGroupNotExist() {
        assertFalse(() -> groupDao.deleteById(21L));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> groupDao.save(new Group("AB-91", faculty, students, lectures)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> groupDao.save(new Group(1001L, "AB-91", faculty, students, lectures)));
    }
}
