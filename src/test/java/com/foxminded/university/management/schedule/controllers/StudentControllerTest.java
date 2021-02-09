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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 1L, 1L),
                new Student(3L, "Minetta", "Funcheon", "Sayle", 2, 2L, 1L));
        when(studentService.getAllStudents()).thenReturn(students);

        List<Group> groups = List.of(
                new Group(1L, "AB-01", 1L, 1L),
                new Group(1L, "AB-01", 1L, 1L),
                new Group(2L, "CD-21", 1L, 1L));
        when(groupService.getGroupsForStudents(students)).thenReturn(groups);

        List<String> groupNames = List.of("AB-01", "AB-01", "CD-21");
        when(groupService.getGroupNamesForStudents(students)).thenReturn(groupNames);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("groupNames", groupNames))
                .andExpect(model().attribute("groups", groups));
    }

}
