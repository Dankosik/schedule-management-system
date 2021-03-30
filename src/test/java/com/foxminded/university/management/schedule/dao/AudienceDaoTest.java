package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AudienceDaoTest extends BaseDaoTest {
    private final Audience audience = new Audience(1L, 101, 45, null);
    private final List<Lecture> lectures = List.of(new Lecture(1L, 101,
                    Date.valueOf(LocalDate.of(2020, 1, 1)), audience, null, null, null),
            new Lecture(2L, 102,
                    Date.valueOf(LocalDate.of(2020, 2, 1)), audience, null, null, null));
    private AudienceDao audienceDao;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        audienceDao = new AudienceDao(entityManager);
    }

    @Test
    void shouldCreateNewAudience() {
        Audience actual = audienceDao.save(new Audience(310, 25, lectures));
        Audience expected = new Audience(actual.getId(), 310, 25, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateAudience() {
        Audience audience = new Audience(1000L, 402, 310, entityManager.find(Audience.class, 1000L).getLectures());

        assertNotEquals(audience, entityManager.find(Audience.class, audience.getId()));

        Audience actual = audienceDao.save(audience);

        assertEquals(audience, actual);
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        Audience actual = audienceDao.getById(1000L).get();
        Audience expected = new Audience(1000L, 301, 50, entityManager.find(Audience.class, 1000L).getLectures());

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfAudiences() {
        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50, entityManager.find(Audience.class, 1000L).getLectures()),
                new Audience(1001L, 302, 75, entityManager.find(Audience.class, 1001L).getLectures()),
                new Audience(1002L, 303, 100, entityManager.find(Audience.class, 1002L).getLectures()),
                new Audience(1003L, 304, 30, entityManager.find(Audience.class, 1003L).getLectures()),
                new Audience(1004L, 305, 55, entityManager.find(Audience.class, 1004L).getLectures()));
        List<Audience> actual = audienceDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteAudience() {
        assertTrue(audienceDao.deleteById(1000L));
        assertFalse(audienceDao.getById(1000L).isPresent());
    }

    @Test
    void shouldSaveListOfAudiences() {
        List<Audience> audiences = List.of(
                new Audience(400, 15, lectures),
                new Audience(401, 60, lectures));

        List<Audience> expected = List.of(
                new Audience(1000L, 301, 50, entityManager.find(Audience.class, 1000L).getLectures()),
                new Audience(1001L, 302, 75, entityManager.find(Audience.class, 1001L).getLectures()),
                new Audience(1002L, 303, 100, entityManager.find(Audience.class, 1002L).getLectures()),
                new Audience(1003L, 304, 30, entityManager.find(Audience.class, 1003L).getLectures()),
                new Audience(1004L, 305, 55, entityManager.find(Audience.class, 1004L).getLectures()),
                new Audience(1L, 400, 15, lectures),
                new Audience(2L, 401, 60, lectures));
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
    @Transactional(propagation = Propagation.NEVER)
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnCreate() {
        assertThrows(DuplicateKeyException.class, () -> audienceDao.save(new Audience(301, 12, lectures)));
    }

    @Test
    void shouldThrowExceptionIfUniquenessConstraintViolatedOnUpdate() {
        assertThrows(DuplicateKeyException.class, () -> audienceDao.save(new Audience(1000L, 303, 12, lectures)));
    }
}
