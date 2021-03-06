package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.web.LectureController;
import com.foxminded.university.management.schedule.controllers.web.utils.DurationFormatter;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LectureController.class)
class LectureControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LectureService lectureService;
    @MockBean
    private AudienceService audienceService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private DurationFormatter durationFormatter;
    @MockBean
    private BindingResult bindingResult;

    @Test
    public void shouldReturnViewWithAllLectures() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null));
        when(lectureService.getAllLectures()).thenReturn(lectures);

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null));
        when(lessonService.getLessonsWithPossibleNullForLectures(lectures)).thenReturn(lessons);

        List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(lessons)).thenReturn(durations);
        List<String> formattedDurations = List.of("1:30", "1:30");

        List<Subject> subjectsForLessons = List.of(new Subject(1L, "Math", null), new Subject(2L, "Art", null));
        when(subjectService.getSubjectsWithPossibleNullForLessons(lessons)).thenReturn(subjectsForLessons);

        List<String> subjectNames = List.of("Math", "Art");
        when(subjectService.getSubjectNamesForLessons(lessons)).thenReturn(subjectNames);

        List<Time> startTimes = List.of(
                Time.valueOf(LocalTime.of(8, 30, 0)),
                Time.valueOf(LocalTime.of(10, 10, 0)));
        when(lessonService.getStartTimesWithPossibleNullForLessons(lessons)).thenReturn(startTimes);

        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null));
        when(teacherService.getTeachersWithPossibleNullForLectures(lectures)).thenReturn(teachers);

        List<Teacher> allTeachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null),
                new Teacher(3L, "Mike", "Mike", "Mike", null, null),
                new Teacher(4L, "Jackson", "Jackson", "Jackson", null, null));
        when(teacherService.getAllTeachers()).thenReturn(allTeachers);

        List<String> teacherNames = List.of("Jackson J. J.", "Conor M. C.");
        when(teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers)).thenReturn(teacherNames);

        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, null),
                new Audience(2L, 302, 55, null));
        when(audienceService.getAudiencesWithPossibleNullForLectures(lectures)).thenReturn(audiences);

        List<Audience> allAudiences = List.of(
                new Audience(1L, 301, 45, null),
                new Audience(2L, 302, 55, null),
                new Audience(3L, 303, 65, null),
                new Audience(4L, 304, 45, null));
        when(audienceService.getAllAudiences()).thenReturn(allAudiences);

        List<Integer> audienceNumbers = List.of(301, 302);
        when(audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences)).thenReturn(audienceNumbers);

        List<Subject> subjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));
        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "AB-11", null, null, null),
                new Group(3L, "CD-21", null, null, null));
        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "AB-11", null, null, null));
        when(groupService.getGroupsForLectures(lectures)).thenReturn(groups);

        List<String> groupNames = List.of("AB-01", "AB-11");
        when(groupService.getGroupNamesForLectures(lectures)).thenReturn(groupNames);

        List<Lesson> allLessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(3L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null));
        when(lessonService.getAllLessons()).thenReturn(allLessons);

        List<Duration> durationsForAllLessons = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(allLessons)).thenReturn(durationsForAllLessons);
        List<String> formattedDurationsForAllLessons = List.of("1:30", "1:30", "1:30");

        List<Subject> subjectsForAllLessons = List.of(new Subject(1L, "Math", null), new Subject(2L, "Art", null), new Subject(2L, "Art", null));
        when(subjectService.getSubjectsWithPossibleNullForLessons(allLessons)).thenReturn(subjectsForAllLessons);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures"))
                .andExpect(model().attribute("lectures", lectures))
                .andExpect(model().attribute("durations", formattedDurations))
                .andExpect(model().attribute("subjectNames", subjectNames))
                .andExpect(model().attribute("startTimes", startTimes))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("allTeachers", allTeachers))
                .andExpect(model().attribute("teacherNames", teacherNames))
                .andExpect(model().attribute("audiences", audiences))
                .andExpect(model().attribute("allAudiences", allAudiences))
                .andExpect(model().attribute("audienceNumbers", audienceNumbers))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("subjectsForLessons", subjectsForLessons))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("allGroups", allGroups))
                .andExpect(model().attribute("allLessons", allLessons))
                .andExpect(model().attribute("durationsForAllLessons", formattedDurationsForAllLessons))
                .andExpect(model().attribute("subjectsForAllLessons", subjectsForAllLessons))
                .andExpect(model().attribute("lecture", new Lecture()));

        verify(lectureService, times(1)).getAllLectures();
        verify(lessonService, times(1)).getLessonsWithPossibleNullForLectures(lectures);
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(lessons);
        verify(subjectService, times(1)).getSubjectNamesForLessons(lessons);
        verify(lessonService, times(1)).getStartTimesWithPossibleNullForLessons(lessons);
        verify(teacherService, times(1)).getTeachersWithPossibleNullForLectures(lectures);
        verify(teacherService, times(1)).getAllTeachers();
        verify(teacherService, times(1)).getLastNamesWithInitialsWithPossibleNullForTeachers(teachers);
        verify(audienceService, times(1)).getAudiencesWithPossibleNullForLectures(lectures);
        verify(audienceService, times(1)).getAllAudiences();
        verify(audienceService, times(1)).getAudienceNumbersWithPossibleNullForAudiences(audiences);
        verify(subjectService, times(1)).getSubjectsForLectures(lectures);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(lessons);
        verify(groupService, times(1)).getGroupNamesForLectures(lectures);
        verify(groupService, times(1)).getAllGroups();
        verify(groupService, times(1)).getGroupsForLectures(lectures);
        verify(lessonService, times(1)).getAllLessons();
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(allLessons);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(allLessons);
    }

    @Test
    public void shouldAddLecture() throws Exception {
        Lecture lecture = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2021, 1, 1)),
                null, null, new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null), null);
        when(lectureService.saveLecture(new Lecture(111, Date.valueOf(LocalDate.of(2021, 1, 1)),
                null, null, new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null), null))).thenReturn(lecture);
        mockMvc.perform(
                post("/lectures/add")
                        .flashAttr("lecture", lecture))
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService, times(1)).saveLecture(new Lecture(111,
                Date.valueOf(LocalDate.of(2021, 1, 1)), null, null,
                new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null), null));
    }

    @Test
    public void shouldUpdateLecture() throws Exception {
        Lecture lecture = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2021, 1, 1)),
                null, null, new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null), null);
        when(lectureService.saveLecture(lecture)).thenReturn(lecture);
        mockMvc.perform(
                post("/lectures/update/{id}", 1L)
                        .flashAttr("lecture", lecture))
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService, times(1)).saveLecture(lecture);
    }

    @Test
    public void shouldDeleteLecture() throws Exception {
        Lecture lecture = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, null, null);
        doNothing().when(lectureService).deleteLectureById(1L);
        mockMvc.perform(
                post("/lectures/delete/{id}", 1L)
                        .flashAttr("lecture", lecture))
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService, times(1)).deleteLectureById(1L);
    }

    @Test
    public void shouldRedirectToLecturesWithValidErrorsOnAdd() throws Exception {
        Lecture lecture = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null), null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("lecture", "date", "Year Error")));
        mockMvc.perform(
                post("/lectures/add")
                        .flashAttr("fieldErrorsOnAdd", bindingResult.getFieldErrors())
                        .flashAttr("lectureWithErrors", new Lecture(lecture.getNumber(), lecture.getDate(),
                                lecture.getAudience(), lecture.getGroup(), lecture.getLesson(), lecture.getTeacher())))
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService, never()).saveLecture(lecture);
    }

    @Test
    public void shouldRedirectToLecturesWithValidErrorsOnUpdate() throws Exception {
        Lecture lecture = new Lecture(1L, 111, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, null, new Lesson(1L, 111, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null), null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("lecture", "date", "Year Error")));
        mockMvc.perform(
                post("/lectures/update/{id}", 1L)
                        .flashAttr("fieldErrorsOnUpdate", bindingResult.getFieldErrors())
                        .flashAttr("lectureWithErrors", new Lecture(lecture.getNumber(), lecture.getDate(),
                                lecture.getAudience(), lecture.getGroup(), lecture.getLesson(), lecture.getTeacher())))
                .andExpect(redirectedUrl("/lectures"))
                .andExpect(view().name("redirect:/lectures"));

        verify(lectureService, never()).saveLecture(lecture);
    }
}
