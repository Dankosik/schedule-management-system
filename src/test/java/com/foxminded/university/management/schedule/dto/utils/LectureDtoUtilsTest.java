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
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LectureDtoUtils.class})
class LectureDtoUtilsTest {
    @MockBean
    private LessonServiceImpl lessonService;
    @MockBean
    private GroupServiceImpl groupService;
    @MockBean
    private TeacherServiceImpl teacherService;
    @MockBean
    private AudienceServiceImpl audienceService;
    private final Faculty faculty = new Faculty(1L, "FAIT", null, null);
    private final Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
            Duration.ofMinutes(90), new Subject(1L, "Math", null), null);
    private final Audience audience = new Audience(1L, 1, 1, null);
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);
    private final Group group = new Group(1L, "AB-01", faculty, null, null);

    @Test
    void shouldReturnTrueIfSuchLessonFromLectureAddDtoExist() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        assertTrue(LectureDtoUtils.isSuchLessonFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null,
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math")), null)));
    }

    @Test
    void shouldReturnFalseIfSuchLessonFromLectureAddDtoNotExist() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        assertFalse(LectureDtoUtils.isSuchLessonFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null,
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Mathsasd")), null)));
    }

    @Test
    void shouldReturnTrueIfSuchLessonFromLectureUpdateDtoExist() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        assertTrue(LectureDtoUtils.isSuchLessonFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null,
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math")), null)));
    }

    @Test
    void shouldReturnFalseIfSuchLessonFromLectureUpdateDtoNotExist() {
        when(lessonService.getLessonById(1L)).thenReturn(new Lesson(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null));
        assertFalse(LectureDtoUtils.isSuchLessonFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null,
                new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(10, 10, 0)),
                        Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Mathasd")), null)));
    }

    @Test
    void shouldReturnTrueIfSuchGroupFromLectureAddDtoExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertTrue(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")), null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchGroupFromLectureAddDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AA-21", new FacultyUpdateDto(1L, "FAIT")), null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchGroupsFacultyFromLectureAddDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAITASDASD")), null, null)));
    }

    @Test
    void shouldReturnTrueIfSuchGroupFromLectureUpdateDtoExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertTrue(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")), null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchGroupFromLectureUpdateDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AA-21", new FacultyUpdateDto(1L, "FAIT")), null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchGroupsFacultyFromLectureUpdateDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(LectureDtoUtils.isSuchGroupFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null,
                new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAITASDASD")), null, null)));
    }

    @Test
    void shouldReturnTrueIfSuchTeacherFromLectureAddDtoExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertTrue(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchTeacherFromLectureAddDtoNotExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertFalse(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "John", "Jackson", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchTeachersFacultyFromLectureAddDtoNotExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertFalse(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FSAAIT")))));
    }

    @Test
    void shouldReturnTrueIfSuchTeacherFromLectureUpdateDtoExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertTrue(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchTeacherFromLectureUpdateDtoNotExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertFalse(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "John", "Jackson", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchTeachersFacultyFromLectureUpdateDtoNotExist() {
        when(teacherService.getTeacherById(1L)).thenReturn(new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null));
        assertFalse(LectureDtoUtils.isSuchTeacherFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), null, null, null,
                new TeacherUpdateDto(1L, "John", "Jackson", "Jackson", new FacultyUpdateDto(1L, "FSADAIT")))));
    }

    @Test
    void shouldReturnTrueIfSuchAudienceFromLectureAddDtoExist() {
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        assertTrue(LectureDtoUtils.isSuchAudienceFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), new AudienceUpdateDto(1L, 1, 1),
                null, null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchAudienceFromLectureAddDtoNotExist() {
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        assertFalse(LectureDtoUtils.isSuchAudienceFromLectureDtoExist(new LectureAddDto(1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), new AudienceUpdateDto(1L, 21, 1),
                null, null, null)));
    }

    @Test
    void shouldReturnTrueIfSuchAudienceFromLectureUpdateDtoExist() {
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        assertTrue(LectureDtoUtils.isSuchAudienceFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), new AudienceUpdateDto(1L, 1, 1),
                null, null, null)));
    }

    @Test
    void shouldReturnFalseIfSuchAudienceFromLectureUpdateDtoNotExist() {
        when(audienceService.getAudienceById(1L)).thenReturn(new Audience(1L, 1, 1, null));
        assertFalse(LectureDtoUtils.isSuchAudienceFromLectureDtoExist(new LectureUpdateDto(1L, 1,
                Date.valueOf(LocalDate.of(2020, 1, 1)), new AudienceUpdateDto(1L, 21, 1),
                null, null, null)));
    }

    @Test
    void shouldReturnLectureFromLectureUpdateDto() {
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
