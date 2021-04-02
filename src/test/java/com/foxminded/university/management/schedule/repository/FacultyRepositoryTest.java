package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Faculty;
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
class FacultyRepositoryTest extends BaseDaoTest {
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnFacultyWithIdOne() {
        Faculty actual = facultyRepository.findById(1000L).get();
        Faculty expected = new Faculty(1000L, "FAIT", entityManager.find(Faculty.class, 1000L).getGroups(),
                entityManager.find(Faculty.class, 1000L).getTeachers());

        assertEquals(expected, actual);
    }
}
