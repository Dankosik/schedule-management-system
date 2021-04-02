package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.DurationFormatter;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LessonController.class)
class LessonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LessonServiceImpl lessonService;
    @MockBean
    private SubjectServiceImpl subjectService;
    @MockBean
    private DurationFormatter durationFormatter;

    @Test
    public void shouldReturnViewWithAllLessons() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");
        List<Lesson> lessons = List.of(
                new Lesson(1000L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(1001L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null));

        when(lessonService.getAllLessons()).thenReturn(lessons);

        List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(lessons)).thenReturn(durations);

        List<String> formattedDurations = List.of("1:30", "1:30");

        List<Subject> subjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));
        when(subjectService.getSubjectsWithPossibleNullForLessons(lessons)).thenReturn(subjects);

        List<Subject> allSubjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null),
                new Subject(3L, "Programming", null));
        when(subjectService.getAllSubjects()).thenReturn(allSubjects);

        List<String> subjectNames = List.of("Math", "Art");
        when(subjectService.getSubjectNamesForLessons(lessons)).thenReturn(subjectNames);

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(view().name("lessons"))
                .andExpect(model().attribute("durations", formattedDurations))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("allSubjects", allSubjects))
                .andExpect(model().attribute("subjectNames", subjectNames))
                .andExpect(model().attribute("lesson", new Lesson()))
                .andExpect(model().attribute("lessons", lessons));

        verify(lessonService, times(1)).getAllLessons();
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(lessons);
        verify(subjectService, times(1)).getSubjectNamesForLessons(lessons);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(lessons);
        verify(subjectService, times(1)).getAllSubjects();
    }

    @Test
    public void shouldAddLesson() throws Exception {
        Lesson lesson = new Lesson(1L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null);
        when(lessonService.saveLesson(new Lesson(2, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null))).thenReturn(lesson);
        mockMvc.perform(
                post("/lessons/add")
                        .flashAttr("lesson", lesson))
                .andExpect(redirectedUrl("/lessons"))
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, times(1)).saveLesson(new Lesson(2,
                Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null));
    }

    @Test
    public void shouldUpdateLesson() throws Exception {
        Lesson lesson = new Lesson(1L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null);
        when(lessonService.saveLesson(lesson)).thenReturn(lesson);
        mockMvc.perform(
                post("/lessons/update/{id}", 1L)
                        .flashAttr("lesson", lesson))
                .andExpect(redirectedUrl("/lessons"))
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, times(1)).saveLesson(lesson);
    }

    @Test
    public void shouldDeleteLesson() throws Exception {
        Lesson lesson = new Lesson(1L, 2, Time.valueOf(LocalTime.of(10, 10, 0)),
                Duration.ofMinutes(90), null, null);
        doNothing().when(lessonService).deleteLessonById(1L);
        mockMvc.perform(
                post("/lessons/delete/{id}", 1L)
                        .flashAttr("lesson", lesson))
                .andExpect(redirectedUrl("/lessons"))
                .andExpect(view().name("redirect:/lessons"));

        verify(lessonService, times(1)).deleteLessonById(1L);
    }
}
