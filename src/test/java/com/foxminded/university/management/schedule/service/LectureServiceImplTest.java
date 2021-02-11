package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.*;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class LectureServiceImplTest {
    private final Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
            null, null, null, null);
    private final List<Lecture> lectures = List.of(lecture,
            new Lecture(2L, 2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    null, null, null, null),
            new Lecture(3L, 3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                    null, null, null, null));
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, 1L);
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L);
    private final Group group = new Group(1L, "AB-01", null, 1L);
    @Autowired
    private LectureServiceImpl lectureService;
    @MockBean
    private LectureDao lectureDao;
    @MockBean
    private LessonDao lessonDao;
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private AudienceDao audienceDao;
    @MockBean
    private GroupDao groupDao;

    @Test
    void shouldSaveLecture() {
        when(lectureDao.save(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null))).thenReturn(lecture);
        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        Lecture actual = lectureService.saveLecture(lecture);

        assertEquals(lecture, actual);

        verify(teacherDao, times(1)).getById(null);
        verify(audienceDao, times(1)).getById(null);
        verify(lessonDao, times(1)).getById(null);
        verify(lectureDao, times(1)).save(lecture);
    }

    @Test
    void shouldReturnLectureWithIdOne() {
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        Lecture actual = lectureService.getLectureById(1L);

        assertEquals(lecture, actual);

        verify(lectureDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfLectures() {
        when(lectureDao.getAll()).thenReturn(lectures);

        assertEquals(lectures, lectureService.getAllLectures());

        verify(lectureDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteLectureWithIdOne() {
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureDao.deleteById(1L)).thenReturn(true);

        lectureService.deleteLectureById(1L);

        verify(lectureDao, times(1)).deleteById(1L);
        verify(lectureDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfLessons() {
        when(lectureDao.save(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null))).thenReturn(lecture);
        when(lectureDao.save(new Lecture(2, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null))).thenReturn(lectures.get(1));
        when(lectureDao.save(new Lecture(3, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null))).thenReturn(lectures.get(2));
        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));

        List<Lecture> actual = lectureService.saveAllLectures(lectures);

        assertEquals(lectures, actual);

        verify(teacherDao, times(3)).getById(null);
        verify(audienceDao, times(3)).getById(null);
        verify(lessonDao, times(3)).getById(null);
        verify(lectureDao, times(1)).save(lectures.get(0));
        verify(lectureDao, times(1)).save(lectures.get(1));
        verify(lectureDao, times(1)).save(lectures.get(2));
    }

    @Test
    void shouldAddLessonToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, 1L, null);

        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.save(expected)).thenReturn(expected);

        Lecture actual = lectureService.addLessonToLecture(lesson, lecture);
        assertEquals(expected, actual);

        verify(lessonDao, times(2)).getById(1L);
        verify(teacherDao, times(1)).getById(null);
        verify(audienceDao, times(1)).getById(null);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(expected);
    }

    @Test
    void shouldRemoveLessonFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null);

        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureService.saveLecture(expected)).thenReturn(expected);

        Lecture actual = lectureService.removeLessonFromLecture(lesson, new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, 1L, null));
        assertEquals(expected, actual);

        verify(teacherDao, times(2)).getById(null);
        verify(audienceDao, times(2)).getById(null);
        verify(lessonDao, times(2)).getById(null);
        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(lecture);
    }

    @Test
    void shouldAddTeacherToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, 1L);

        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureDao.save(expected)).thenReturn(expected);
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        Lecture actual = lectureService.addTeacherToLecture(teacher, lecture);
        assertEquals(expected, actual);

        verify(audienceDao, times(1)).getById(null);
        verify(lessonDao, times(1)).getById(null);
        verify(teacherDao, times(2)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(expected);
    }

    @Test
    void shouldRemoveTeacherFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null);

        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureService.saveLecture(expected)).thenReturn(expected);

        Lecture actual = lectureService.removeTeacherFromLecture(teacher, new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, 1L));
        assertEquals(expected, actual);

        verify(teacherDao, times(2)).getById(null);
        verify(audienceDao, times(2)).getById(null);
        verify(lessonDao, times(2)).getById(null);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(expected);
    }

    @Test
    void shouldAddGroupToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, 1L, null, null);
        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(groupDao.getById(1L))
                .thenReturn(Optional.of(new Group(1L, "AB-01", 1L, 1L)));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureDao.save(expected)).thenReturn(expected);
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        Lecture actual = lectureService.addGroupToLecture(group, lecture);
        assertEquals(expected, actual);

        verify(audienceDao, times(1)).getById(null);
        verify(lessonDao, times(1)).getById(null);
        verify(groupDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(null);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(expected);
    }

    @Test
    void shouldRemoveGroupFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null);
        when(teacherDao.getById(null))
                .thenReturn(Optional.of(new Teacher(null, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(null))
                .thenReturn(Optional.of(new Audience(null, 202, 45, 1L)));
        when(lessonDao.getById(null))
                .thenReturn(Optional.of(new Lesson(null, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L)));
        when(groupDao.getById(1L))
                .thenReturn(Optional.of(new Group(1L, "AB-01", 1L, 1L)));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureDao.save(expected)).thenReturn(expected);
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        Lecture actual = lectureService.removeGroupFromLecture(group,
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)), null, 1L, null, null));
        assertEquals(expected, actual);

        verify(audienceDao, times(1)).getById(null);
        verify(lessonDao, times(1)).getById(null);
        verify(groupDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(null);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureWithInputIdNotFound() {
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.getLectureById(1L));

        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLessonNotPresentInAddingLessonToLecture() {
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.addLessonToLecture(lesson, lecture));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInAddingLessonToLecture() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.addLessonToLecture(lesson, lecture));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfTeacherNotPresentInAddingTeacherToLecture() {
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.addTeacherToLecture(teacher, lecture));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInAddingGroupToLecture() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.addGroupToLecture(group, lecture));

        verify(groupDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInAddingGroupToLecture() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.addGroupToLecture(group, lecture));

        verify(groupDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInRemovingGroupFromLecture() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.removeGroupFromLecture(group, lecture));

        verify(groupDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInRemovingGroupFromLecture() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.removeGroupFromLecture(group, lecture));

        verify(groupDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInAddingTeacherToLecture() {
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.addTeacherToLecture(teacher, lecture));


        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLessonNotPresentInRemovingLessonFromLecture() {
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.removeLessonFromLecture(lesson, lecture));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInRemovingLessonFromLecture() {
        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.removeLessonFromLecture(lesson, lecture));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfTeacherNotPresentInRemovingTeacherFromLecture() {
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> lectureService.removeTeacherFromLecture(teacher, lecture));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInRemovingTeacherFromLecture() {
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.removeTeacherFromLecture(teacher, lecture));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(lecture);
    }

    @Test
    void shouldThrowExceptionIfLessonIsAlreadyAddedToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 3L);

        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.addLessonToLecture(lesson, expected));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfTeacherIsAlreadyAddedToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 1L);

        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.addTeacherToLecture(teacher, expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLessonIsAlreadyRemovedFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, null, 3L);

        when(lessonDao.getById(1L)).thenReturn(Optional.of(lesson));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.removeLessonFromLecture(lesson, expected));

        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfGroupIsAlreadyAddedToLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.addGroupToLecture(group, expected));

        verify(groupDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfGroupIsAlreadyRemovedFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, null, null, 3L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.removeGroupFromLecture(group, expected));

        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfTeacherIsAlreadyRemovedFromLecture() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, null);

        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> lectureService.removeTeacherFromLecture(teacher, expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureTeacherNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 1L);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureLessonNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 1L);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L))
                .thenReturn(Optional.of(new Teacher(1L, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(1L))
                .thenReturn(Optional.of(new Audience(1L, 202, 45, 1L)));
        when(lessonDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(audienceDao, times(1)).getById(1L);
        verify(lessonDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureAudienceNotFound() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 1L, 1L, 1L);

        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));
        when(teacherDao.getById(1L))
                .thenReturn(Optional.of(new Teacher(1L, "John", "Jackson", "Jackson", null, 1L)));
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> lectureService.saveLecture(expected));

        verify(teacherDao, times(1)).getById(1L);
        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, never()).save(expected);
    }

    @Test
    void shouldReturnLecturesForAudience() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 2L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 3L, 2L, 2L));

        when(lectureDao.getLecturesByAudienceId(1L)).thenReturn(expected);

        assertEquals(expected, lectureService.getLecturesForAudience(new Audience(1L, 211, 35, 1L)));

        verify(lectureDao, times(1)).getLecturesByAudienceId(1L);
    }

    @Test
    void shouldReturnLecturesForTeacher() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 2L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 3L, 2L, 1L));

        when(lectureDao.getLecturesByTeacherId(1L)).thenReturn(expected);

        assertEquals(expected, lectureService.getLecturesForTeacher(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L)));

        verify(lectureDao, times(1)).getLecturesByTeacherId(1L);
    }

    @Test
    void shouldReturnLecturesForGroup() {
        List<Lecture> expected = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 2L, 1L));

        when(lectureDao.getLecturesByGroupId(1L)).thenReturn(expected);

        assertEquals(expected, lectureService.getLecturesForGroup(new Group(1L, "AB-12", 1L, 1L)));

        verify(lectureDao, times(1)).getLecturesByGroupId(1L);
    }
}
