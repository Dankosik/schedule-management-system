package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Lecture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class LectureRepositoryTest extends BaseDaoTest {
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnListOfLecture() {
        List<Lecture> expected = List.of(
                new Lecture(1000L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1000L).getAudience(),
                        entityManager.find(Lecture.class, 1000L).getGroup(),
                        entityManager.find(Lecture.class, 1000L).getLesson(),
                        entityManager.find(Lecture.class, 1000L).getTeacher()),
                new Lecture(1001L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1001L).getAudience(),
                        entityManager.find(Lecture.class, 1001L).getGroup(),
                        entityManager.find(Lecture.class, 1001L).getLesson(),
                        entityManager.find(Lecture.class, 1001L).getTeacher()),
                new Lecture(1002L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        entityManager.find(Lecture.class, 1002L).getAudience(),
                        entityManager.find(Lecture.class, 1002L).getGroup(),
                        entityManager.find(Lecture.class, 1002L).getLesson(),
                        entityManager.find(Lecture.class, 1002L).getTeacher()),
                new Lecture(1003L, 4, Date.valueOf(LocalDate.of(2021, 2, 1)),
                        entityManager.find(Lecture.class, 1003L).getAudience(),
                        entityManager.find(Lecture.class, 1003L).getGroup(),
                        entityManager.find(Lecture.class, 1003L).getLesson(),
                        entityManager.find(Lecture.class, 1003L).getTeacher()));
        List<Lecture> actual = lectureRepository.findAll();

        assertTrue(actual.containsAll(expected));
    }
}
