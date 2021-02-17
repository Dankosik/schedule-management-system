package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupController.class)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LectureServiceImpl lectureService;
    @MockBean
    private AudienceServiceImpl audienceService;
    @MockBean
    private LessonServiceImpl lessonService;
    @MockBean
    private TeacherServiceImpl teacherService;
    @MockBean
    private SubjectServiceImpl subjectService;
    @MockBean
    private StudentServiceImpl studentService;
    @MockBean
    private GroupServiceImpl groupService;

    @Test
    public void shouldReturnViewWithAllGroups() throws Exception {
        List<Group> groups = List.of(
                new Group(1L, "AB-01", 1L, 1L),
                new Group(2L, "CD-21", 1L, 1L));

        when(groupService.getAllGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attribute("groups", groups));
    }

    @Test
    public void shouldReturnViewWithOneGroup() throws Exception {
        Group group = new Group(1L, "AB-01", 1L, 1L);
        when(groupService.getGroupById(1L)).thenReturn(group);

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 1L, 2L, 1L));
        when(lectureService.getLecturesForGroup(group)).thenReturn(lectures);

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), 1L),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), 2L));
        when(lessonService.getLessonsForLectures(lectures)).thenReturn(lessons);

        List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsForLessons(lessons)).thenReturn(durations);
        List<String> formattedDurations = List.of("1:30:00", "1:30:00");

        List<String> subjectNames = List.of("Math", "Art");
        when(subjectService.getSubjectNamesForLessons(lessons)).thenReturn(subjectNames);

        List<Time> startTimes = List.of(
                Time.valueOf(LocalTime.of(8, 30, 0)),
                Time.valueOf(LocalTime.of(10, 10, 0)));
        when(lessonService.getStartTimesForLessons(lessons)).thenReturn(startTimes);

        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L));
        when(teacherService.getTeachersForLectures(lectures)).thenReturn(teachers);

        List<String> teacherNames = List.of("Jackson J. J.", "Conor M. C.");
        when(teacherService.getLastNamesWithInitialsForTeachers(teachers)).thenReturn(teacherNames);

        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, 1L),
                new Audience(2L, 302, 55, 1L));
        when(audienceService.getAudiencesForLectures(lectures)).thenReturn(audiences);

        List<Integer> audienceNumbers = List.of(301, 302);
        when(audienceService.getAudienceNumbersForAudiences(audiences)).thenReturn(audienceNumbers);

        List<Subject> subjects = List.of(
                new Subject(1L, "Math", 1L),
                new Subject(2L, "Art", 1L));
        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1000L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L, 1000L));
        when(studentService.getStudentsForGroup(group)).thenReturn(students);

        mockMvc.perform(get("/groups/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attribute("lectures", lectures))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("durations", formattedDurations))
                .andExpect(model().attribute("subjectNames", subjectNames))
                .andExpect(model().attribute("startTimes", startTimes))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("teacherNames", teacherNames))
                .andExpect(model().attribute("audienceNumbers", audienceNumbers))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("group", group));
    }

    @Test
    public void shouldDeleteGroup() throws Exception {
        Group group = new Group(1L, "AB-01", 1L, 1L);
        given(groupService.getGroupById(1L)).willReturn(group);
        doNothing().when(groupService).deleteGroupById(1L);
        mockMvc.perform(post("/groups/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection());
    }
}
