package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
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
@WebMvcTest(FacultyController.class)
class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyServiceImpl facultyService;
    @MockBean
    private GroupServiceImpl groupService;
    @MockBean
    private TeacherServiceImpl teacherService;

    @Test
    public void shouldReturnViewWithAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT", 1L),
                new Faculty(2L, "FKFN", 1L));

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties"))
                .andExpect(model().attribute("faculties", faculties));
    }

    @Test
    public void shouldReturnViewWithOneFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", 1L);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        List<Group> groups = List.of(
                new Group(1L, "AG-01", 1L, 1L),
                new Group(2L, "GD-02", 1L, 1L));
        when(groupService.getGroupsForFaculty(faculty)).thenReturn(groups);

        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", 1L, 1L),
                new Teacher("Mike", "Conor", "Conor", 1L, 1L));
        when(teacherService.getTeachersForFaculty(faculty)).thenReturn(teachers);

        mockMvc.perform(get("/faculties/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("faculty", faculty));
    }
}
