package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
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
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherServiceImpl teacherService;
    @MockBean
    private FacultyServiceImpl facultyService;

    @Test
    public void shouldReturnViewWithAllTeachers() throws Exception {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT", 1L),
                new Faculty(2L, "FKFN", 1L));
        when(facultyService.getFacultiesForTeachers(teachers)).thenReturn(faculties);

        List<String> facultyNames = List.of("FAIT", "FKFN");
        when(facultyService.getFacultyNamesForTeachers(teachers)).thenReturn(facultyNames);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers"))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("faculties", faculties))
                .andExpect(model().attribute("facultyNames", facultyNames));
    }

    @Test
    public void shouldReturnViewWithOneTeacher() throws Exception {
        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        Faculty faculty = new Faculty(1L, "FAIT", 1L);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teacher"))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attribute("faculty", faculty));
    }
}
