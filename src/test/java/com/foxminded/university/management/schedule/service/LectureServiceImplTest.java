package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.repository.AudienceRepository;
import com.foxminded.university.management.schedule.repository.LectureRepository;
import com.foxminded.university.management.schedule.repository.LessonRepository;
import com.foxminded.university.management.schedule.repository.TeacherRepository;
import com.foxminded.university.management.schedule.service.impl.LectureServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LectureServiceImpl.class})
class LectureServiceImplTest {
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
            Duration.ofMinutes(90), null, null);
    private final Audience audience = new Audience(1L, 202, 45, null);
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
    private final Lecture lecture = new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
            audience, null, lesson, teacher);
    private final List<Lecture> lectures = List.of(lecture,
            new Lecture(2L, 2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    audience, null, lesson, teacher),
            new Lecture(3L, 3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    audience, null, lesson, teacher));
    @Autowired
    private LectureServiceImpl lectureService;
    @MockBean
    private LectureRepository lectureRepository;
    @MockBean
    private LessonRepository lessonRepository;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private AudienceRepository audienceRepository;

    @Test
    void shouldSaveLecture() {
        when(lectureRepository.save(lecture)).thenReturn(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher));
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));
        when(audienceRepository.findById(1L))
                .thenReturn(Optional.of(audience));
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.of(lesson));
        Lecture actual = lectureService.saveLecture(lecture);

        assertEquals(lecture, actual);

        verify(teacherRepository, times(1)).findById(1L);
        verify(audienceRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).findById(1L);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        Lecture actual = lectureService.getLectureById(1L);

        assertEquals(lecture, actual);

        verify(lectureRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfLectures() {
        when(lectureRepository.findAll()).thenReturn(lectures);

        assertEquals(lectures, lectureService.getAllLectures());

        verify(lectureRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteLectureWithIdOne() {
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher)));

        lectureService.deleteLectureById(1L);

        verify(lectureRepository, times(1)).deleteById(1L);
        verify(lectureRepository, times(2)).findById(1L);
    }

    @Test
    void shouldSaveListOfLessons() {
        when(lectureRepository.save(lecture)).thenReturn(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher));
        when(teacherRepository.findById(1L))
                .thenReturn(Optional.of(teacher));
        when(audienceRepository.findById(1L))
                .thenReturn(Optional.of(audience));
        when(lessonRepository.findById(1L))
                .thenReturn(Optional.of(lesson));

        when(lectureRepository.save(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lecture);
        when(lectureRepository.save(new Lecture(2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lectures.get(1));
        when(lectureRepository.save(new Lecture(3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher))).thenReturn(lectures.get(2));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(audienceRepository.findById(1L)).thenReturn(Optional.of(audience));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        List<Lecture> actual = lectureService.saveAllLectures(lectures);

        assertEquals(lectures, actual);

        verify(teacherRepository, times(3)).findById(1L);
        verify(audienceRepository, times(3)).findById(1L);
        verify(lessonRepository, times(3)).findById(1L);
        verify(lectureRepository, times(1)).save(lectures.get(0));
        verify(lectureRepository, times(1)).save(lectures.get(1));
        verify(lectureRepository, times(1)).save(lectures.get(2));
    }

    @Test
    void shouldThrowExceptionIfLectureWithInputIdNotFound() {
        when(lectureRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.getLectureById(1L));

        verify(lectureRepository, times(1)).findById(1L);
        verify(lectureRepository, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureTeacherNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherRepository, times(1)).findById(1L);
        verify(lectureRepository, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureLessonNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(audienceRepository.findById(1L)).thenReturn(Optional.of(audience));
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherRepository, times(1)).findById(1L);
        verify(audienceRepository, times(1)).findById(1L);
        verify(lessonRepository, times(1)).findById(1L);
        verify(lectureRepository, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureAudienceNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, null, lesson, teacher);

        when(lectureRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(audienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherRepository, times(1)).findById(1L);
        verify(audienceRepository, times(1)).findById(1L);
        verify(lectureRepository, never()).save(expected);
    }

    @Test
    void shouldReturnTrueIfLectureWithIdExist() {
        when(lectureRepository.findById(1L)).thenReturn(Optional.of(new Lecture(3,
                Date.valueOf(LocalDate.of(2020, 1, 1)), audience, null, lesson, teacher)));
        assertTrue(lectureService.isLectureWithIdExist(1L));
    }

    @Test
    void shouldReturnFalseIfLectureWithIdNotExist() {
        when(lectureRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(lectureService.isLectureWithIdExist(1L));
    }
}
