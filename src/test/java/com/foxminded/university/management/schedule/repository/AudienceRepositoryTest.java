package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AudienceRepositoryTest extends BaseDaoTest {
    @Autowired
    private AudienceRepository audienceRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnAudienceWithIdOne() {
        Audience actual = audienceRepository.findById(1000L).get();
        Audience expected = new Audience(1000L, 301, 50, entityManager.find(Audience.class, 1000L).getLectures());

        assertEquals(expected, actual);
    }
}
