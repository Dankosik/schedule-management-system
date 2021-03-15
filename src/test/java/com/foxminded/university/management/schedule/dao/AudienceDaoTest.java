package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AudienceDaoTest extends BaseDaoTest {
    private AudienceDao audienceDao;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        audienceDao = new AudienceDao(entityManager);
    }

    @Test
    void shouldCreateNewAudience() {
        Audience expected = new Audience(310, 25);
        Long audienceId = audienceDao.save(new Audience(310, 25)).getId();
        assertTrue(testUtils.existsById("audiences", audienceId));

        Map<String, Object> map = testUtils.getEntry("audiences", audienceId);
        Audience actual = new Audience((Integer) map.get("number"), (Integer) map.get("capacity"));
        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateAudience() {
        Audience audience = new Audience(1000L, 310, 25);
        Long audienceId = audienceDao.save(audience).getId();
        assertTrue(testUtils.existsById("audiences", audienceId));

        Map<String, Object> map = testUtils.getEntry("audiences", audienceId);
        Audience actual = new Audience((Long) map.get("id"), (Integer) map.get("number"), (Integer) map.get("capacity"));
        assertEquals(audience, actual);
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("audiences", 1000L);
        Audience expected = new Audience((Long) map.get("id"), (Integer) map.get("number"), (Integer) map.get("capacity"));
        Audience actual = audienceDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfAudiences() {
        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50),
                new Audience(1001L, 302, 75),
                new Audience(1002L, 303, 100),
                new Audience(1003L, 304, 30),
                new Audience(1004L, 305, 55));
        List<Audience> actual = audienceDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteAudience() {
        assertTrue(audienceDao.deleteById(1000L));
        assertFalse(testUtils.existsById("audiences", 1000L));
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Audience> audiences = List.of(
                new Audience(400, 15),
                new Audience(401, 60));

        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50),
                new Audience(1001L, 302, 75),
                new Audience(1002L, 303, 100),
                new Audience(1003L, 304, 30),
                new Audience(1004L, 305, 55),
                new Audience(1L, 400, 15),
                new Audience(2L, 401, 60));
        audienceDao.saveAll(audiences);
        List<Audience> actual = audienceDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindAudienceNotExist() {
        assertFalse(audienceDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfAudienceNotExist() {
        assertFalse(() -> audienceDao.deleteById(21L));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> audienceDao.save(new Audience(301, 12)));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> audienceDao.save(new Audience(1000L, 303, 12)));
    }
}
