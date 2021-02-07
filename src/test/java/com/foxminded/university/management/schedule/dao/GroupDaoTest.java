package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Group;
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
class GroupDaoTest extends BaseDaoTest {
    private GroupDao groupDao;

    @BeforeEach
    void setUp() {
        groupDao = new GroupDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewGroup() {
        Group group = new Group("AB-81", 1000L, 1000L);
        Long groupId = groupDao.save(group).getId();
        assertTrue(testUtils.existsById("groups", groupId));

        Map<String, Object> map = testUtils.getEntry("groups", groupId);
        Group actual = new Group((String) map.get("name"), (Long) map.get("faculty_id"), (Long) map.get("university_id"));
        assertEquals(group, actual);
    }

    @Test
    void shouldUpdateGroup() {
        Group group = new Group(1000L, "AB-81", 1000L, 1000L);
        Long groupId = groupDao.save(group).getId();
        assertTrue(testUtils.existsById("groups", groupId));

        Map<String, Object> map = testUtils.getEntry("groups", groupId);
        Group actual = new Group((Long) map.get("id"), (String) map.get("name"), (Long) map.get("faculty_id"),
                (Long) map.get("university_id"));
        assertEquals(group, actual);
    }

    @Test
    void shouldReturnGroupWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("groups", 1000L);
        Group expected = new Group((Long) map.get("id"), (String) map.get("name"), (Long) map.get("faculty_id"),
                (Long) map.get("university_id"));
        Group actual = groupDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroups() {
        List<Group> expected = List.of(
                new Group(1000L, "AB-91", 1000L, 1000L),
                new Group(1001L, "BC-01", 1001L, 1000L));
        List<Group> actual = groupDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteGroup() {
        assertTrue(groupDao.deleteById(1000L));
        assertFalse(testUtils.existsById("groups", 1000L));
    }

    @Test
    void shouldSaveListOfGroups() {
        List<Group> groups = List.of(
                new Group("CD-71", 1000L, 1000L),
                new Group("IF-61", 1001L, 1000L));

        List<Group> expected = List.of(
                new Group(1000L, "AB-91", 1000L, 1000L),
                new Group(1001L, "BC-01", 1001L, 1000L),
                new Group(1L, "CD-71", 1000L, 1000L),
                new Group(2L, "IF-61", 1001L, 1000L));
        groupDao.saveAll(groups);
        List<Group> actual = groupDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfGroupsWithUniversityIdOne() {
        List<Group> expected = List.of(
                new Group(1000L, "AB-91", 1000L, 1000L),
                new Group(1001L, "BC-01", 1001L, 1000L));
        List<Group> actual = groupDao.getGroupsByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfGroupsWithFacultyIdOne() {
        List<Group> expected = List.of(
                new Group(1000L, "AB-91", 1000L, 1000L));
        List<Group> actual = groupDao.getGroupsByFacultyId(1000L);

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
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> groupDao.save(new Group("AB-91", 1000L, 1000L)));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> groupDao.save(new Group(1001L, "AB-91", 1000L, 1000L)));
    }
}
