package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.web.GroupController;
import com.foxminded.university.management.schedule.controllers.web.utils.DurationFormatter;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
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
@WebMvcTest(GroupController.class)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
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
    private FacultyServiceImpl facultyService;
    @MockBean
    private DurationFormatter durationFormatter;
    @MockBean
    private BindingResult bindingResult;

    @Test
    public void shouldReturnViewWithAllGroups() throws Exception {
        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "CD-21", null, null, null));

        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<Faculty> allFaculties = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null),
                new Faculty(3L, "NSAX", null, null));
        when(facultyService.getAllFaculties()).thenReturn(allFaculties);

        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(1L, "FAIT", null, null));
        when(facultyService.getFacultiesForGroups(allGroups)).thenReturn(faculties);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attribute("allGroups", allGroups))
                .andExpect(model().attribute("group", new Group()))
                .andExpect(model().attribute("faculties", faculties))
                .andExpect(model().attribute("allFaculties", allFaculties));

        verify(groupService, times(1)).getAllGroups();
        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldReturnViewWithOneGroup() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");
        Group group = new Group(1L, "AB-01", null, null, null);
        when(groupService.getGroupById(1L)).thenReturn(group);

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, group, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, group, null, null));
        group.setLectures(lectures);

        List<Lesson> lessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, lectures),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, lectures));
        when(lessonService.getLessonsWithPossibleNullForLectures(lectures)).thenReturn(lessons);

        List<Duration> durations = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(lessons)).thenReturn(durations);
        List<String> formattedDurations = List.of("1:30", "1:30");

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
            lectures.get(i).setTeacher(teachers.get(0));
        }

        when(teacherService.getTeachersWithPossibleNullForLectures(lectures)).thenReturn(teachers);

        List<String> teacherNames = List.of("Jackson J. J.", "Conor M. C.");
        when(teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers)).thenReturn(teacherNames);

        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, lectures),
                new Audience(2L, 302, 55, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(0).setAudience(audiences.get(0));
        }

        when(audienceService.getAudiencesWithPossibleNullForLectures(lectures)).thenReturn(audiences);

        List<Integer> audienceNumbers = List.of(301, 302);
        when(audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences)).thenReturn(audienceNumbers);

        List<Subject> subjects = List.of(
                new Subject(1L, "Math", lessons),
                new Subject(2L, "Art", lessons));
        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, group),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, group));
        group.setStudents(students);

        List<Teacher> allTeachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, lectures),
                new Teacher(2L, "Mike", "Conor", "Conor", null, lectures),
                new Teacher(3L, "John", "Conor", "John", null, lectures));
        when(teacherService.getAllTeachers()).thenReturn(allTeachers);

        List<Audience> allAudiences = List.of(
                new Audience(1L, 301, 45, lectures),
                new Audience(2L, 302, 55, lectures),
                new Audience(3L, 303, 65, lectures));
        when(audienceService.getAllAudiences()).thenReturn(allAudiences);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", null, students, lectures),
                new Group(2L, "AB-11", null, students, lectures),
                new Group(3L, "AC-21", null, students, lectures));
        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<Lesson> allLessons = List.of(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, lectures),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(10, 10, 0)), Duration.ofMinutes(90), null, lectures),
                new Lesson(3L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), null, lectures));
        when(lessonService.getAllLessons()).thenReturn(allLessons);

        List<Duration> durationsForAllLessons = List.of(Duration.ofMinutes(90), Duration.ofMinutes(90), Duration.ofMinutes(90));
        when(lessonService.getDurationsWithPossibleNullForLessons(allLessons)).thenReturn(durationsForAllLessons);
        List<String> formattedDurationsForAllLessons = List.of("1:30", "1:30", "1:30");

        List<Subject> allSubjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null),
                new Subject(2L, "Programming", null));
        when(subjectService.getSubjectsWithPossibleNullForLessons(allLessons)).thenReturn(allSubjects);

        Faculty faculty = new Faculty(1L, "FAIT", null, teachers);
        group.setFaculty(faculty);

        mockMvc.perform(get("/groups/{id}", 1L))
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
                .andExpect(model().attribute("lecture", new Lecture()))
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("allTeachers", allTeachers))
                .andExpect(model().attribute("allAudiences", allAudiences))
                .andExpect(model().attribute("allGroups", allGroups))
                .andExpect(model().attribute("allLessons", allLessons))
                .andExpect(model().attribute("durationsForAllLessons", formattedDurationsForAllLessons))
                .andExpect(model().attribute("subjectsForAllLessons", allSubjects))
                .andExpect(model().attribute("faculty", faculty))
                .andExpect(model().attribute("group", group));

        verify(groupService, times(1)).getGroupById(1L);
        verify(lessonService, times(1)).getLessonsWithPossibleNullForLectures(lectures);
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(lessons);
        verify(lessonService, times(1)).getStartTimesWithPossibleNullForLessons(lessons);
        verify(subjectService, times(1)).getSubjectNamesForLessons(lessons);
        verify(subjectService, times(1)).getSubjectsForLectures(lectures);
        verify(teacherService, times(1)).getTeachersWithPossibleNullForLectures(lectures);
        verify(teacherService, times(1)).getLastNamesWithInitialsWithPossibleNullForTeachers(teachers);
        verify(audienceService, times(1)).getAudiencesWithPossibleNullForLectures(lectures);
        verify(audienceService, times(1)).getAudienceNumbersWithPossibleNullForAudiences(audiences);
        verify(teacherService, times(1)).getAllTeachers();
        verify(audienceService, times(1)).getAllAudiences();
        verify(groupService, times(1)).getAllGroups();
        verify(lessonService, times(1)).getAllLessons();
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(allLessons);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(allLessons);
    }

    @Test
    public void shouldAddGroup() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(groupService.saveGroup(new Group("AB-01", null, null, null))).thenReturn(group);
        mockMvc.perform(
                post("/groups/add")
                        .flashAttr("group", group))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, times(1)).saveGroup(new Group("AB-01", null, null, null));
    }

    @Test
    public void shouldReturnFormWithErrorOnAddGroup() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(groupService.saveGroup(new Group("AB-01", null, null, null))).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/groups/add")
                        .flashAttr("group", group))
                .andExpect(flash().attribute("newGroup", group))
                .andExpect(flash().attribute("group", new Group()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnAdd",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, times(1)).saveGroup(new Group("AB-01", null, null, null));
    }

    @Test
    public void shouldUpdateGroup() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(groupService.saveGroup(group)).thenReturn(group);
        mockMvc.perform(
                post("/groups/update/{id}", 1L)
                        .flashAttr("group", group))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    public void shouldReturnFormWithErrorOnUpdateGroup() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(groupService.saveGroup(group)).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/groups/update/{id}", 1L)
                        .flashAttr("group", group))
                .andExpect(flash().attribute("newGroup", group))
                .andExpect(flash().attribute("group", new Group()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnUpdate",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    public void shouldDeleteGroup() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        doNothing().when(groupService).deleteGroupById(1L);
        mockMvc.perform(
                post("/groups/delete/{id}", 1L)
                        .flashAttr("group", group))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, times(1)).deleteGroupById(1L);
    }

    @Test
    public void shouldRedirectToGroupsWithValidErrorsOnAdd() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("group", "name", "Group name Error")));
        mockMvc.perform(
                post("/groups/add")
                        .flashAttr("fieldErrorsOnAdd", bindingResult.getFieldErrors())
                        .flashAttr("groupWithErrors", new Group(group.getName(), group.getFaculty(),
                                group.getStudents(), group.getLectures())))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldRedirectToGroupsWithValidErrorsOnUpdate() throws Exception {
        Group group = new Group(1L, "AB-01", null, null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("group", "name", "Group name Error")));
        mockMvc.perform(
                post("/groups/update/{id}", 1L)
                        .flashAttr("fieldErrorsOnUpdate", bindingResult.getFieldErrors())
                        .flashAttr("groupWithErrors", new Group(group.getName(), group.getFaculty(),
                                group.getStudents(), group.getLectures())))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(view().name("redirect:/groups"));

        verify(groupService, never()).saveGroup(group);
    }
}
