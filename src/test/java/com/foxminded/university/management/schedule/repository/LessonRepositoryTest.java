package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class LessonRepositoryTest extends BaseDaoTest {
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    private EntityManager entityManager;
    private Subject subject;
    private List<Lecture> lectures;

    @BeforeEach
    void setUp() {
        subject = entityManager.find(Lesson.class, 1000L).getSubject();
        lectures = entityManager.find(Lesson.class, 1000L).getLectures();
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        Lesson actual = lessonRepository.findById(1000L).get();
        Lesson expected = new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfLessons() {
        List<Lesson> expected = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, lectures),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1001L).getSubject(), entityManager.find(Lesson.class, 1001L).getLectures()),
                new Lesson(1002L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1002L).getSubject(), entityManager.find(Lesson.class, 1002L).getLectures()),
                new Lesson(1003L, 4, Time.valueOf(LocalTime.of(13, 20, 0)), Duration.ofMinutes(90),
                        entityManager.find(Lesson.class, 1003L).getSubject(), entityManager.find(Lesson.class, 1003L).getLectures()));
        List<Lesson> actual = lessonRepository.findAll();

        assertTrue(actual.containsAll(expected));
    }
}
