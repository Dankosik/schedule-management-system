package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.repository.LessonRepository;
import com.foxminded.university.management.schedule.repository.SubjectRepository;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LessonServiceImpl.class})
class LessonServiceImplTest {
    private final Subject subject = new Subject(1L, "Art", null);
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);
    private final List<Lesson> lessons = List.of(lesson,
            new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null),
            new Lesson(3L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), subject, null));

    @Autowired
    private LessonServiceImpl lessonService;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private SubjectRepository subjectRepository;

    @Test
    void shouldSaveLesson() {
        when(lessonRepository.save(new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null))).thenReturn(lesson);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        Lesson actual = lessonService.saveLesson(lesson);

        assertEquals(lesson, actual);

        verify(lessonRepository, times(1)).save(lesson);
        verify(subjectRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        Lesson actual = lessonService.getLessonById(1L);

        assertEquals(lesson, actual);

        verify(lessonRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfLessons() {
        when(lessonRepository.findAll()).thenReturn(lessons);

        assertEquals(lessons, lessonService.getAllLessons());

        verify(lessonRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteLessonWithIdOne() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        lessonService.deleteLessonById(1L);

        verify(lessonRepository, times(1)).deleteById(1L);
        verify(lessonRepository, times(2)).findById(1L);
    }

    @Test
    void shouldSaveListOfLessons() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(lessonRepository.save(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null)))
                .thenReturn(lesson);
        when(lessonRepository.save(new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null)))
                .thenReturn(lessons.get(1));
        when(lessonRepository.save(new Lesson(3L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), subject, null)))
                .thenReturn(lessons.get(2));

        List<Lesson> actual = lessonService.saveAllLessons(lessons);

        assertEquals(lessons, actual);

        verify(lessonRepository, times(1)).save(lessons.get(0));
        verify(lessonRepository, times(1)).save(lessons.get(1));
        verify(lessonRepository, times(1)).save(lessons.get(2));
        verify(subjectRepository, times(3)).findById(1L);
    }

    @Test
    void shouldThrowExceptionIfLessonWithInputIdNotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lessonService.getLessonById(1L));

        verify(lessonRepository, times(1)).findById(1L);
        verify(lessonRepository, never()).save(lesson);
    }

    @Test
    void shouldReturnDurationsForLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        List<Duration> expected = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));

        assertEquals(expected, lessonService.getDurationsWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnDurationsForLessonsWithDurationNull() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), null, subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        List<Duration> expected = Arrays.asList(null, Duration.ofMinutes(90));

        assertEquals(expected, lessonService.getDurationsWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnStartTimesForLessons() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        List<Time> expected = List.of(
                Time.valueOf(LocalTime.of(8, 30, 0)),
                Time.valueOf(LocalTime.of(10, 10, 0)));

        assertEquals(expected, lessonService.getStartTimesWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnStartTimesForLessonsWithStartTimeNull() {
        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, null, Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        List<Time> expected = Arrays.asList(null, Time.valueOf(LocalTime.of(10, 10, 0)));

        assertEquals(expected, lessonService.getStartTimesWithPossibleNullForLessons(lessons));
    }

    @Test
    void shouldReturnLessonsForLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null), null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null), null));

        List<Lesson> expected = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        assertEquals(expected, lessonService.getLessonsWithPossibleNullForLectures(lectures));
    }

    @Test
    void shouldReturnLessonsForLecturesWithLessonIdZero() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                        new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null), null));

        List<Lesson> expected = Arrays.asList(
                null, new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), subject, null));

        assertEquals(expected, lessonService.getLessonsWithPossibleNullForLectures(lectures));
    }
}
