package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
    private BindingResult bindingResult;

    @Test
    public void shouldReturnViewWithAllFaculties() throws Exception {
        List<Faculty> faculties = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null));

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
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        List<Group> groups = List.of(
                new Group(1L, "AG-01", faculty, null, null),
                new Group(2L, "GD-02", faculty, null, null));
        faculty.setGroups(groups);
        List<Teacher> teachers = List.of(
                new Teacher("John", "Jackson", "Jackson", faculty, null),
                new Teacher("Mike", "Conor", "Conor", faculty, null));
        faculty.setTeachers(teachers);
        List<Faculty> allFaculties = List.of(new Faculty(1L, "FAIT", groups, teachers),
                new Faculty(2L, "FKFN", groups, teachers));
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
        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldAddFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(facultyService.saveFaculty(new Faculty("FAIT", null, null))).thenReturn(faculty);
        mockMvc.perform(
                post("/faculties/add")
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(new Faculty("FAIT", null, null));
    }

    @Test
    public void shouldReturnFormWithErrorOnAddFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(facultyService.saveFaculty(new Faculty("FAIT", null, null))).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/faculties/add")
                        .flashAttr("faculty", faculty))
                .andExpect(flash().attribute("newFaculty", faculty))
                .andExpect(flash().attribute("faculty", new Faculty()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnAdd",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(new Faculty("FAIT", null, null));
    }

    @Test
    public void shouldUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(facultyService.saveFaculty(faculty)).thenReturn(faculty);
        mockMvc.perform(
                post("/faculties/update/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnFormWithErrorOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(facultyService.saveFaculty(faculty)).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/faculties/update/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(flash().attribute("newFaculty", faculty))
                .andExpect(flash().attribute("faculty", new Faculty()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnUpdate",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        doNothing().when(facultyService).deleteFacultyById(1L);
        mockMvc.perform(
                post("/faculties/delete/{id}", 1L)
                        .flashAttr("faculty", faculty))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, times(1)).deleteFacultyById(1L);
    }

    @Test
    public void shouldRedirectToFacultiesWithValidErrorsOnAdd() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("faculty", "name",
                "Must not contain digits and spaces, all letters must be capital")));
        mockMvc.perform(
                post("/faculties/add")
                        .flashAttr("fieldErrors", bindingResult.getFieldErrors())
                        .flashAttr("facultyWithErrorsOnAdd", new Faculty(faculty.getName(), faculty.getGroups(), faculty.getTeachers())))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, never()).saveFaculty(faculty);
    }

    @Test
    public void shouldRedirectToFacultiesWithValidErrorsOnUpdate() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("faculty", "name",
                "Must not contain digits and spaces, all letters must be capital")));
        mockMvc.perform(
                post("/faculties/update/{id}", 1L)
                        .flashAttr("fieldErrors", bindingResult.getFieldErrors())
                        .flashAttr("facultyWithErrorsOnAdd", new Faculty(faculty.getId(), faculty.getName(), faculty.getGroups(), faculty.getTeachers())))
                .andExpect(redirectedUrl("/faculties"))
                .andExpect(view().name("redirect:/faculties"));

        verify(facultyService, never()).saveFaculty(faculty);
    }
}
