package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.DurationFormatter;
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
import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyServiceImpl facultyService;
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
    private GroupServiceImpl groupService;
    @MockBean
    private DurationFormatter durationFormatter;

    @Test
    public void shouldReturnViewWithAllTeachers() throws Exception {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT"),
                new Faculty(2L, "FKFN"));
        when(facultyService.getFacultiesForTeachers(teachers)).thenReturn(faculties);

        List<Faculty> allFaculties = List.of(
                new Faculty(1L, "FAIT"),
                new Faculty(2L, "FKFN"),
                new Faculty(3L, "QNS"));
        when(facultyService.getAllFaculties()).thenReturn(allFaculties);

        List<String> facultyNames = List.of("FAIT", "FKFN");
        when(facultyService.getFacultyNamesForTeachers(teachers)).thenReturn(facultyNames);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers"))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("teacher", new Teacher()))
                .andExpect(model().attribute("faculties", faculties))
                .andExpect(model().attribute("allFaculties", allFaculties))
                .andExpect(model().attribute("facultyNames", facultyNames));
    }

    @Test
    public void shouldReturnViewWithOneTeacher() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", 1L);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 1L));
        when(lectureService.getLecturesForTeacher(teacher)).thenReturn(lectures);

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

        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45),
                new Audience(2L, 302, 55));
        when(audienceService.getAudiencesForLectures(lectures)).thenReturn(audiences);

        List<Integer> audienceNumbers = List.of(301, 302);
        when(audienceService.getAudienceNumbersForAudiences(audiences)).thenReturn(audienceNumbers);

        List<Subject> subjects = List.of(
                new Subject(1L, "Math"),
                new Subject(2L, "Art"));
        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(2L, "AB-11", 1L));
        when(groupService.getGroupsForLectures(lectures)).thenReturn(groups);

        List<String> groupNames = List.of("AB-01", "AB-11");
        when(groupService.getGroupNamesForLectures(lectures)).thenReturn(groupNames);

        mockMvc.perform(get("/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher"))
                .andExpect(model().attribute("lectures", lectures))
                .andExpect(model().attribute("durations", formattedDurations))
                .andExpect(model().attribute("subjectNames", subjectNames))
                .andExpect(model().attribute("startTimes", startTimes))
                .andExpect(model().attribute("audienceNumbers", audienceNumbers))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attribute("faculty", faculty));
    }

    @Test
    public void shouldDeleteTeacher() throws Exception {
        Teacher teacher = new Teacher("John", "Jackson", "Jackson", 1L);
        given(teacherService.getTeacherById(1L)).willReturn(teacher);
        doNothing().when(teacherService).deleteTeacherById(1L);
        mockMvc.perform(post("/teachers/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection());
    }
}
