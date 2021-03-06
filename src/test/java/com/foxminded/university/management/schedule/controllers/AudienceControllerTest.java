package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.web.AudienceController;
import com.foxminded.university.management.schedule.controllers.web.utils.DurationFormatter;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
@WebMvcTest(AudienceController.class)
class AudienceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AudienceService audienceService;
    @MockBean
    private LessonService lessonService;
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private DurationFormatter durationFormatter;
    @MockBean
    private BindingResult bindingResult;

    @Test
    public void shouldReturnViewWithAllAudiences() throws Exception {
        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, null),
                new Audience(2L, 302, 55, null));

        when(audienceService.getAllAudiences()).thenReturn(audiences);

        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences"))
                .andExpect(model().attribute("audiences", audiences))
                .andExpect(model().attribute("audience", new Audience()));

        verify(audienceService, times(1)).getAllAudiences();
    }

    @Test
    public void shouldReturnViewWithOneAudience() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");

        Audience audience = new Audience(1L, 301, 45, null);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), audience, null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), audience, null, null, null));
        audience.setLectures(lectures);

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, lectures),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(i).setLesson(lessons.get(i));
        }

        when(lessonService.getLessonsWithPossibleNullForLectures(lectures)).thenReturn(lessons);

        List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(lessons)).thenReturn(durations);
        List<String> formattedDurations = List.of("1:30", "1:30");

        List<Subject> subjects = List.of(
                new Subject(1L, "Math", lessons),
                new Subject(2L, "Art", lessons));

        for (int i = 0; i < lessons.size(); i++) {
            lessons.get(i).setSubject(subjects.get(i));
        }

        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);
        List<String> subjectNames = List.of("Math", "Art");
        when(subjectService.getSubjectNamesForLessons(lessons)).thenReturn(subjectNames);

        List<Time> startTimes = List.of(
                Time.valueOf(LocalTime.of(8, 30, 0)),
                Time.valueOf(LocalTime.of(10, 10, 0)));
        when(lessonService.getStartTimesWithPossibleNullForLessons(lessons)).thenReturn(startTimes);

        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, lectures),
                new Teacher(2L, "Mike", "Conor", "Conor", null, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(i).setTeacher(teachers.get(i));
        }

        when(teacherService.getTeachersWithPossibleNullForLectures(lectures)).thenReturn(teachers);

        List<String> teacherNames = List.of("Jackson J. J.", "Conor M. C.");
        when(teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers)).thenReturn(teacherNames);

        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, lectures),
                new Audience(2L, 302, 55, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(i).setAudience(audiences.get(i));
        }

        when(audienceService.getAudiencesWithPossibleNullForLectures(lectures)).thenReturn(audiences);

        List<Integer> audienceNumbers = List.of(301, 302);
        when(audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences)).thenReturn(audienceNumbers);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", null, null, lectures),
                new Group(2L, "AB-11", null, null, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(i).setGroup(groups.get(i));
        }

        when(groupService.getGroupsForLectures(lectures)).thenReturn(groups);

        List<String> groupNames = List.of("AB-01", "AB-11");
        when(groupService.getGroupNamesForLectures(lectures)).thenReturn(groupNames);

        List<Teacher> allTeachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null),
                new Teacher(3L, "John", "Conor", "John", null, null));
        when(teacherService.getAllTeachers()).thenReturn(allTeachers);

        List<Audience> allAudiences = List.of(
                new Audience(1L, 301, 45, null),
                new Audience(2L, 302, 55, null),
                new Audience(3L, 303, 65, null));
        when(audienceService.getAllAudiences()).thenReturn(allAudiences);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "AB-11", null, null, null),
                new Group(3L, "AC-21", null, null, null));
        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<Lesson> allLessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, null),
                new Lesson(3L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, null));
        when(lessonService.getAllLessons()).thenReturn(allLessons);

        List<Duration> durationsForAllLessons = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(allLessons)).thenReturn(durationsForAllLessons);
        List<String> formattedDurationsForAllLessons = List.of("1:30", "1:30", "1:30");

        List<Subject> allSubjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null),
                new Subject(2L, "Programming", null));
        when(subjectService.getSubjectsWithPossibleNullForLessons(allLessons)).thenReturn(allSubjects);

        mockMvc.perform(get("/audiences/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("audience"))
                .andExpect(model().attribute("lectures", lectures))
                .andExpect(model().attribute("durations", formattedDurations))
                .andExpect(model().attribute("subjectNames", subjectNames))
                .andExpect(model().attribute("startTimes", startTimes))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("teacherNames", teacherNames))
                .andExpect(model().attribute("subjects", subjects))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("lecture", new Lecture()))
                .andExpect(model().attribute("allTeachers", allTeachers))
                .andExpect(model().attribute("allAudiences", allAudiences))
                .andExpect(model().attribute("allGroups", allGroups))
                .andExpect(model().attribute("allLessons", allLessons))
                .andExpect(model().attribute("durationsForAllLessons", formattedDurationsForAllLessons))
                .andExpect(model().attribute("subjectsForAllLessons", allSubjects))
                .andExpect(model().attribute("audience", audience));

        verify(audienceService, times(1)).getAudienceById(1L);
        verify(lessonService, times(1)).getLessonsWithPossibleNullForLectures(lectures);
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(lessons);
        verify(lessonService, times(1)).getStartTimesWithPossibleNullForLessons(lessons);
        verify(teacherService, times(1)).getTeachersWithPossibleNullForLectures(lectures);
        verify(teacherService, times(1)).getLastNamesWithInitialsWithPossibleNullForTeachers(teachers);
        verify(subjectService, times(1)).getSubjectsForLectures(lectures);
        verify(groupService, times(1)).getGroupNamesForLectures(lectures);
        verify(groupService, times(1)).getGroupsForLectures(lectures);
        verify(teacherService, times(1)).getAllTeachers();
        verify(audienceService, times(1)).getAllAudiences();
        verify(groupService, times(1)).getAllGroups();
        verify(lessonService, times(1)).getAllLessons();
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(allLessons);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(allLessons);
    }

    @Test
    public void shouldAddAudience() throws Exception {
        Audience audience = new Audience(1L, 201, 25, null);
        when(audienceService.saveAudience(new Audience(201, 25, null))).thenReturn(audience);
        mockMvc.perform(
                post("/audiences/add")
                        .flashAttr("audience", audience))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, times(1)).saveAudience(new Audience(201, 25, null));
    }

    @Test
    public void shouldRedirectToAudiencesWithValidErrorsOnAdd() throws Exception {
        Audience audience = new Audience(1L, 1001, 25, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("audience", "number", "Must be no more than 999")));
        mockMvc.perform(
                post("/audiences/add")
                        .flashAttr("fieldErrors", bindingResult.getFieldErrors())
                        .flashAttr("audienceWithErrors", new Audience(audience.getNumber(), audience.getCapacity(), audience.getLectures())))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, never()).saveAudience(new Audience(201, 25, null));
    }

    @Test
    public void shouldRedirectToAudiencesWithValidErrorsOnUpdate() throws Exception {
        Audience audience = new Audience(1L, 1001, 25, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("audience", "number", "Must be no more than 999")));
        mockMvc.perform(
                post("/audiences/update/{id}", 1L)
                        .flashAttr("fieldErrorsOnUpdate", bindingResult.getFieldErrors())
                        .flashAttr("audienceWithErrors", new Audience(audience.getId(), audience.getNumber(),
                                audience.getCapacity(), audience.getLectures())))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, never()).saveAudience(new Audience(201, 25, null));
    }

    @Test
    public void shouldReturnFormWithErrorOnAddAudience() throws Exception {
        Audience audience = new Audience(1L, 201, 25, null);
        when(audienceService.saveAudience(new Audience(201, 25, null))).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/audiences/add", 1L)
                        .flashAttr("audience", audience))
                .andExpect(flash().attribute("newAudience", audience))
                .andExpect(flash().attribute("audience", new Audience()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnAdd",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, times(1)).saveAudience(new Audience(201, 25, null));
    }


    @Test
    public void shouldUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 201, 25, null);
        when(audienceService.saveAudience(audience)).thenReturn(audience);
        mockMvc.perform(
                post("/audiences/update/{id}", 1L)
                        .flashAttr("audience", audience))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldReturnFormWithErrorOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 201, 25, null);
        when(audienceService.saveAudience(audience)).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/audiences/update/{id}", 1L)
                        .flashAttr("audience", audience))
                .andExpect(flash().attribute("newAudience", audience))
                .andExpect(flash().attribute("audience", new Audience()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnUpdate",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldDeleteAudience() throws Exception {
        Audience audience = new Audience(1L, 201, 25, null);
        doNothing().when(audienceService).deleteAudienceById(1L);
        mockMvc.perform(
                post("/audiences/delete/{id}", 1L)
                        .flashAttr("audience", audience))
                .andExpect(redirectedUrl("/audiences"))
                .andExpect(view().name("redirect:/audiences"));

        verify(audienceService, times(1)).deleteAudienceById(1L);
    }
}
