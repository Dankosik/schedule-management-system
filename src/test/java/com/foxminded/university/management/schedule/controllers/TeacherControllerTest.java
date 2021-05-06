package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.web.TeacherController;
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
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyService facultyService;
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
    public void shouldReturnViewWithAllTeachers() throws Exception {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null));
        when(facultyService.getFacultiesForTeachers(teachers)).thenReturn(faculties);

        List<Faculty> allFaculties = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null),
                new Faculty(3L, "QNS", null, null));
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

        verify(teacherService, times(1)).getAllTeachers();
        verify(facultyService, times(1)).getFacultyNamesForTeachers(teachers);
        verify(facultyService, times(1)).getFacultiesForTeachers(teachers);
        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldReturnViewWithOneTeacher() throws Exception {
        when(durationFormatter.print(Duration.ofMinutes(90), Locale.getDefault())).thenReturn("1:30:00");
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        teacher.setFaculty(faculty);
        faculty.setTeachers(List.of(teacher));
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, teacher),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, teacher));
        teacher.setLectures(lectures);

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
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));

        for (int i = 0; i < lectures.size(); i++) {
            subjects.get(0).setLessons(lessons);
        }

        when(subjectService.getSubjectsForLectures(lectures)).thenReturn(subjects);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", faculty, null, lectures),
                new Group(2L, "AB-11", faculty, null, lectures));

        for (int i = 0; i < lectures.size(); i++) {
            lectures.get(0).setGroup(groups.get(0));
        }

        faculty.setGroups(groups);
        when(groupService.getGroupsForLectures(lectures)).thenReturn(groups);

        List<String> groupNames = List.of("AB-01", "AB-11");
        when(groupService.getGroupNamesForLectures(lectures)).thenReturn(groupNames);

        List<Faculty> allFaculties = List.of(
                new Faculty(1L, "FAIT", groups, null),
                new Faculty(2L, "FKFN", groups, null),
                new Faculty(3L, "FASW", groups, null));
        when(facultyService.getAllFaculties()).thenReturn(allFaculties);

        List<Teacher> allTeachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", faculty, lectures),
                new Teacher(2L, "Mike", "Conor", "Conor", faculty, lectures),
                new Teacher(3L, "John", "Conor", "John", faculty, lectures));
        when(teacherService.getAllTeachers()).thenReturn(allTeachers);

        List<Audience> allAudiences = List.of(
                new Audience(1L, 301, 45, lectures),
                new Audience(2L, 302, 55, lectures),
                new Audience(3L, 303, 65, lectures));
        when(audienceService.getAllAudiences()).thenReturn(allAudiences);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", faculty, null, lectures),
                new Group(2L, "AB-11", faculty, null, lectures),
                new Group(3L, "AC-21", faculty, null, lectures));
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
                new Subject(1L, "Math", lessons),
                new Subject(2L, "Art", lessons),
                new Subject(2L, "Programming", lessons));
        when(subjectService.getSubjectsWithPossibleNullForLessons(allLessons)).thenReturn(allSubjects);

        mockMvc.perform(get("/teachers/{id}", 1L))
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
                .andExpect(model().attribute("allFaculties", allFaculties))
                .andExpect(model().attribute("allTeachers", allTeachers))
                .andExpect(model().attribute("allAudiences", allAudiences))
                .andExpect(model().attribute("allGroups", allGroups))
                .andExpect(model().attribute("allLessons", allLessons))
                .andExpect(model().attribute("lecture", new Lecture()))
                .andExpect(model().attribute("durationsForAllLessons", formattedDurationsForAllLessons))
                .andExpect(model().attribute("subjectsForAllLessons", allSubjects))
                .andExpect(model().attribute("faculty", faculty));

        verify(teacherService, times(1)).getTeacherById(1L);
        verify(lessonService, times(1)).getLessonsWithPossibleNullForLectures(lectures);
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(lessons);
        verify(lessonService, times(1)).getStartTimesWithPossibleNullForLessons(lessons);
        verify(subjectService, times(1)).getSubjectsForLectures(lectures);
        verify(subjectService, times(1)).getSubjectNamesForLessons(lessons);
        verify(groupService, times(1)).getGroupNamesForLectures(lectures);
        verify(groupService, times(1)).getGroupsForLectures(lectures);
        verify(audienceService, times(1)).getAudiencesWithPossibleNullForLectures(lectures);
        verify(audienceService, times(1)).getAudienceNumbersWithPossibleNullForAudiences(audiences);
        verify(facultyService, times(1)).getAllFaculties();
        verify(teacherService, times(1)).getAllTeachers();
        verify(audienceService, times(1)).getAllAudiences();
        verify(groupService, times(1)).getAllGroups();
        verify(lessonService, times(1)).getAllLessons();
        verify(lessonService, times(1)).getDurationsWithPossibleNullForLessons(allLessons);
        verify(subjectService, times(1)).getSubjectsWithPossibleNullForLessons(allLessons);
    }

    @Test
    public void shouldAddTeacher() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        when(teacherService.saveTeacher(new Teacher("John", "Jackson", "Jackson", null, null)))
                .thenReturn(teacher);
        mockMvc.perform(
                post("/teachers/add")
                        .flashAttr("teacher", teacher))
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, times(1))
                .saveTeacher(new Teacher("John", "Jackson", "Jackson", null, null));
    }

    @Test
    public void shouldUpdateTeacher() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        when(teacherService.saveTeacher(teacher)).thenReturn(teacher);
        mockMvc.perform(
                post("/teachers/update/{id}", 1L)
                        .flashAttr("teacher", teacher))
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, times(1)).saveTeacher(teacher);
    }

    @Test
    public void shouldDeleteTeacher() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        doNothing().when(teacherService).deleteTeacherById(1L);
        mockMvc.perform(
                post("/teachers/delete/{id}", 1L)
                        .flashAttr("teacher", teacher))
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, times(1)).deleteTeacherById(1L);
    }

    @Test
    public void shouldRedirectToTeachersWithValidErrorsOnAdd() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("faculty", "name",
                "Must not contain digits and spaces, all letters must be capital")));
        mockMvc.perform(
                post("/teachers/add")
                        .flashAttr("fieldErrors", bindingResult.getFieldErrors())
                        .flashAttr("teacherWithErrorsOnAdd", new Teacher(teacher.getFirstName(), teacher.getLastName(),
                                teacher.getMiddleName(), teacher.getFaculty(), teacher.getLectures())))
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldRedirectToTeachersWithValidErrorsOnUpdate() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("teacher", "name",
                "Must not contain digits and spaces, all letters must be capital")));
        mockMvc.perform(
                post("/teachers/update/{id}", 1L)
                        .flashAttr("fieldErrorsOnUpdate", bindingResult.getFieldErrors())
                        .flashAttr("teacherWithErrorsOnAdd", new Teacher(teacher.getId(), teacher.getFirstName(),
                                teacher.getLastName(), teacher.getMiddleName(), teacher.getFaculty(), teacher.getLectures())))
                .andExpect(redirectedUrl("/teachers"))
                .andExpect(view().name("redirect:/teachers"));

        verify(teacherService, never()).saveTeacher(teacher);
    }
}
