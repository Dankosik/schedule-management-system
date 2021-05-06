package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.audience.AudienceUpdateDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureAddDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureUpdateDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.AudienceService;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.LessonService;
import com.foxminded.university.management.schedule.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LectureDtoUtils.class})
class LectureDtoUtilsTest {
    private final Faculty faculty = new Faculty(1L, "FAIT", null, null);
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
            Duration.ofMinutes(90), new Subject(1L, "Math", null), null);
    private final Audience audience = new Audience(1L, 1, 1, null);
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);
    private final Group group = new Group(1L, "AB-01", faculty, null, null);
    @MockBean
    private LessonService lessonService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private AudienceService audienceService;

    @Test
    void shouldReturnLectureFromLectureUpdateDto() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, group, lesson, teacher);
        LectureUpdateDto lectureUpdateDto = new LectureUpdateDto(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                new AudienceUpdateDto(1L, 1, 1), new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")),
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math")),
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FAIT")));
        assertEquals(expected, LectureDtoUtils.mapLectureDtoOnLecture(lectureUpdateDto));
    }

    @Test
    void shouldReturnStudentFromStudentAddDto() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                audience, group, lesson, teacher);
        LectureAddDto lectureAddDto = new LectureAddDto(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                new AudienceUpdateDto(1L, 1, 1), new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")),
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math")),
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FAIT")));
        assertEquals(expected, LectureDtoUtils.mapLectureDtoOnLecture(lectureAddDto));
    }
}
