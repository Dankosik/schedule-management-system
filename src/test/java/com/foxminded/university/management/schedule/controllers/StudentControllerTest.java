package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentServiceImpl studentService;
    @MockBean
    private GroupServiceImpl groupService;

    @Test
    public void shouldReturnViewWithAllStudents() throws Exception {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 2L));
        when(studentService.getAllStudents()).thenReturn(students);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(1L, "AB-01", 1L),
                new Group(2L, "CD-21", 1L));
        when(groupService.getGroupsForStudents(students)).thenReturn(groups);

        List<Group> allGroups = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(2L, "CD-21", 1L),
                new Group(3L, "CD-22", 1L),
                new Group(4L, "FB-01", 2L));
        when(groupService.getAllGroups()).thenReturn(allGroups);

        List<String> groupNames = List.of("AB-01", "AB-01", "CD-21");
        when(groupService.getGroupNamesForStudents(students)).thenReturn(groupNames);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("student", new Student()))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("allGroups", allGroups));

        verify(studentService, times(1)).getAllStudents();
        verify(groupService, times(1)).getGroupNamesForStudents(students);
        verify(groupService, times(1)).getGroupsForStudents(students);
        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    public void shouldAddStudent() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L);
        when(studentService.saveStudent(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, 1L)))
                .thenReturn(student);
        mockMvc.perform(
                post("/students/add")
                        .flashAttr("student", student))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1))
                .saveStudent(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, 1L));
    }

    @Test
    public void shouldUpdateStudent() throws Exception {
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L);
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
        Student student = new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L);
        doNothing().when(studentService).deleteStudentById(1L);
        mockMvc.perform(
                post("/students/delete/{id}", 1L)
                        .flashAttr("student", student))
                .andExpect(redirectedUrl("/students"))
                .andExpect(view().name("redirect:/students"));

        verify(studentService, times(1)).deleteStudentById(1L);
    }
}
