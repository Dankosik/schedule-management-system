package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.exceptions.LessonServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LessonServiceImplTest {
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null);
    private final List<Lesson> lessons = List.of(lesson,
            new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null),
            new Lesson(3L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), null));
    private final Subject subject = new Subject(1L, "Art", 1L);

    @Autowired
    private LessonServiceImpl lessonService;
    @MockBean
    private LessonDao lessonDao;
    @MockBean
    private SubjectDao subjectDao;

    @Test
    void shouldSaveLesson() {
        when(lessonDao.save(new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), null))).thenReturn(lesson);
        when(subjectDao.getById(null)).thenReturn(Optional.of(new Subject(null, "Art", 1L)));
        Lesson actual = lessonService.saveLesson(lesson);

        assertEquals(lesson, actual);

        verify(lessonDao, times(1)).save(lesson);
        verify(subjectDao, times(1)).getById(null);
    }

    @Test
    void shouldReturnLessonWithIdOne() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        Lesson actual = lessonService.getLessonById(1L);

        assertEquals(lesson, actual);

        verify(lessonDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfLessons() {
        when(lessonDao.getAll()).thenReturn(lessons);

        assertEquals(lessons, lessonService.getAllLessons());

        verify(lessonDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteLessonWithIdOne() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lessonDao.deleteById(1L)).thenReturn(true);

        lessonService.deleteLessonById(1L);

        verify(lessonDao, times(1)).deleteById(1L);
        verify(lessonDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfLessons() {
        when(subjectDao.getById(null)).thenReturn(Optional.of(new Subject(null, "Art", 1L)));
        when(lessonDao.save(new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null)))
                .thenReturn(lesson);
        when(lessonDao.save(new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null)))
                .thenReturn(lessons.get(1));
        when(lessonDao.save(new Lesson(3L, 3, Time.valueOf(LocalTime.of(11, 50, 0)), Duration.ofMinutes(90), null)))
                .thenReturn(lessons.get(2));

        List<Lesson> actual = lessonService.saveAllLessons(lessons);

        assertEquals(lessons, actual);

        verify(lessonDao, times(1)).save(lessons.get(0));
        verify(lessonDao, times(1)).save(lessons.get(1));
        verify(lessonDao, times(1)).save(lessons.get(2));
        verify(subjectDao, times(3)).getById(null);
    }

    @Test
    void shouldAddSubjectToLesson() {
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L);

        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lessonDao.save(expected)).thenReturn(expected);
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        Lesson actual = lessonService.addSubjectToLesson(subject, lesson);
        assertEquals(expected, actual);

        verify(subjectDao, times(2)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).save(expected);
    }

    @Test
    void shouldRemoveSubjectFromLesson() {
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null);

        when(subjectDao.getById(null)).thenReturn(Optional.of(new Subject(null, "Art", 1L)));
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));
        when(lessonService.saveLesson(expected)).thenReturn(expected);

        Lesson actual = lessonService.removeSubjectFromLesson(subject,
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L));
        assertEquals(expected, actual);

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).save(expected);
        verify(subjectDao, times(2)).getById(null);
    }

    @Test
    void shouldThrowExceptionIfLessonWithInputIdNotFound() {
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(LessonServiceException.class, () -> lessonService.getLessonById(1L));

        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, never()).save(lesson);
    }

    @Test
    void shouldThrowExceptionIfSubjectNotPresentInAddingSubjectToLesson() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(subjectDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(LessonServiceException.class, () -> lessonService.addSubjectToLesson(subject, lesson));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, never()).getById(1L);
        verify(lessonDao, never()).save(lesson);
    }

    @Test
    void shouldThrowExceptionIfLessonNotPresentInAddingSubjectToLesson() {
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        assertThrows(LessonServiceException.class, () -> lessonService.addSubjectToLesson(subject, lesson));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, never()).save(lesson);
    }

    @Test
    void shouldThrowExceptionIfSubjectNotPresentInRemovingSubjectFrom() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(subjectDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(LessonServiceException.class, () -> lessonService.removeSubjectFromLesson(subject, lesson));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, never()).getById(1L);
        verify(lessonDao, never()).save(lesson);
    }

    @Test
    void shouldThrowExceptionIfLessonNotPresentInRemovingSubjectFrom() {
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        assertThrows(LessonServiceException.class, () -> lessonService.removeSubjectFromLesson(subject, lesson));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, never()).save(lesson);
    }

    @Test
    void shouldThrowExceptionIfSubjectIsAlreadyAddedToLesson() {
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L);

        when(lessonDao.getById(1L)).thenReturn(Optional.of(expected));
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        assertThrows(LessonServiceException.class, () -> lessonService.addSubjectToLesson(subject, expected));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfSubjectIsAlreadyRemovedFromLesson() {
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null);

        when(lessonDao.getById(1L)).thenReturn(Optional.of(expected));
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        assertThrows(LessonServiceException.class, () -> lessonService.removeSubjectFromLesson(subject, expected));

        verify(subjectDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lessonDao, never()).save(expected);
    }
}
