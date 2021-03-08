package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                new Faculty(1L, "FAIT"),
                new Faculty(2L, "FKFN"));

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(view().name("faculties"))
                .andExpect(model().attribute("faculties", faculties))
                .andExpect(model().attribute("faculty", new Faculty()));

        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldReturnViewWithOneFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        List<Group> groups = List.of(
                new Group(1L, "AG-01", 1L),
                new Group(2L, "GD-02", 1L));
        when(groupService.getGroupsForFaculty(faculty)).thenReturn(groups);

        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", 1L),
                new Teacher("Mike", "Conor", "Conor", 1L));
        when(teacherService.getTeachersForFaculty(faculty)).thenReturn(teachers);

        List<Faculty> allFaculties = List.of(new Faculty(1L, "FAIT"), new Faculty(2L, "FKFN"));
        when(facultyService.getAllFaculties()).thenReturn(allFaculties);
        mockMvc.perform(get("/faculties/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("faculty"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("teachers", teachers))
                .andExpect(model().attribute("group", new Group()))
                .andExpect(model().attribute("teacher", new Teacher()))
                .andExpect(model().attribute("allFaculties", allFaculties))
                .andExpect(model().attribute("faculty", faculty));

        verify(facultyService, times(1)).getFacultyById(1L);
        verify(groupService, times(1)).getGroupsForFaculty(faculty);
        verify(teacherService, times(1)).getTeachersForFaculty(faculty);
        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldAddFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.saveFaculty(new Faculty("FAIT"))).thenReturn(faculty);
        mockMvc.perform(
                post("/faculties/add")
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(new Faculty("FAIT"));
    }

    @Test
    public void shouldReturnErrorPageOnAddFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.saveFaculty(new Faculty("FAIT"))).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/faculties/add")
                        .flashAttr("faculty", faculty))
                .andExpect(model().attribute("newFaculty", faculty))
                .andExpect(model().attribute("faculty",new Faculty()))
                .andExpect(MockMvcResultMatchers.model().attribute(
                        "exception",
                        Matchers.isA(ServiceException.class)))
                .andExpect(view().name("error/faculty-add-error-page"));

        verify(facultyService, times(1)).saveFaculty(new Faculty("FAIT"));
    }

    @Test
    public void shouldUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.saveFaculty(faculty)).thenReturn(faculty);
        mockMvc.perform(
                post("/faculties/update/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnErrorPageOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        when(facultyService.saveFaculty(faculty)).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/faculties/update/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(model().attribute("newFaculty", faculty))
                .andExpect(model().attribute("faculty",new Faculty()))
                .andExpect(MockMvcResultMatchers.model().attribute(
                        "exception",
                        Matchers.isA(ServiceException.class)))
                .andExpect(view().name("error/faculty-edit-error-page"));

        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT");
        doNothing().when(facultyService).deleteFacultyById(1L);
        mockMvc.perform(
                post("/faculties/delete/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).deleteFacultyById(1L);
    }
}
