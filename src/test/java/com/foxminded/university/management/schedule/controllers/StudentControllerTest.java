package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.web.StudentController;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService studentService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private BindingResult bindingResult;

    @Test
    public void shouldReturnViewWithAllStudents() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, null),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, null));
        when(studentService.getAllStudents()).thenReturn(students);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "CD-21", null, null, null));
        when(groupService.getGroupsWithPossibleNullForStudents(students)).thenReturn(groups);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "CD-21", null, null, null),
                new Group(3L, "CD-22", null, null, null),
                new Group(4L, "FB-01", null, null, null));
        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<String> groupNames = List.of("AB-01", "AB-01", "CD-21");
        when(groupService.getGroupNamesWithPossibleNullForStudents(students)).thenReturn(groupNames);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("allGroups", allGroups));

        verify(studentService, times(1)).getAllStudents();
        verify(groupService, times(1)).getGroupNamesWithPossibleNullForStudents(students);
        verify(groupService, times(1)).getGroupsWithPossibleNullForStudents(students);
        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    public void shouldAddStudent() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null);
        when(studentService.saveStudent(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, null)))
                .thenReturn(student);
        mockMvc.perform(
                post("/students/add")
                        .flashAttr("student", student))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1))
                .saveStudent(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, null));
    }

    @Test
    public void shouldUpdateStudent() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null);
        when(studentService.saveStudent(student)).thenReturn(student);
        mockMvc.perform(
                post("/students/update/{id}", 1L)
                        .flashAttr("student", student))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    public void shouldDeleteStudent() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null);
        doNothing().when(studentService).deleteStudentById(1L);
        mockMvc.perform(
                post("/students/delete/{id}", 1L)
                        .flashAttr("student", student))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).deleteStudentById(1L);
    }

    @Test
    public void shouldRedirectToStudentsWithValidErrorsOnAdd() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("student", "firstName", "Name error")));
        mockMvc.perform(
                post("/students/add")
                        .flashAttr("fieldErrorsOnAdd", bindingResult.getFieldErrors())
                        .flashAttr("studentWithErrors", new Student(student.getFirstName(), student.getLastName(),
                                student.getMiddleName(), student.getCourseNumber(), student.getGroup())))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldRedirectToStudentsWithValidErrorsOnUpdate() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("student", "firstName", "Name error")));
        mockMvc.perform(
                post("/students/update/{id}", 1L)
                        .flashAttr("fieldErrorsOnUpdate", bindingResult.getFieldErrors())
                        .flashAttr("studentWithErrors", new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                                student.getCourseNumber(), student.getGroup())))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, never()).saveStudent(student);
    }
}
