package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
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
@WebMvcTest(SubjectController.class)
class SubjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectServiceImpl subjectService;

    @Test
    public void shouldReturnViewWithAllSubjects() throws Exception {
        List<Subject> subjects = List.of(
                new Subject(1L, "Math", null),
                new Subject(2L, "Art", null));

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjects));
        verify(subjectService, times(1)).getAllSubjects();
    }

    @Test
    public void shouldReturnViewWithOneSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", null);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        mockMvc.perform(get("/subjects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("subject"))
                .andExpect(model().attribute("subject", subject));
    }

    @Test
    public void shouldAddSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", null);
        when(subjectService.saveSubject(new Subject("Art", null))).thenReturn(subject);
        mockMvc.perform(
                post("/subjects/add")
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(new Subject("Art", null));
    }

    @Test
    public void shouldReturnFormWithErrorOnAddSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", null);
        when(subjectService.saveSubject(new Subject("Art", null))).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/subjects/add")
                        .flashAttr("subject", subject))
                .andExpect(flash().attribute("newSubject", subject))
                .andExpect(flash().attribute("subject", new Subject()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnAdd",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(new Subject("Art", null));
    }

    @Test
    public void shouldUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", null);
        when(subjectService.saveSubject(subject)).thenReturn(subject);
        mockMvc.perform(
                post("/subjects/update/{id}", 1L)
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(subject);
    }

    @Test
    public void shouldReturnFormWithErrorOnUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", null);
        when(subjectService.saveSubject(subject)).thenThrow(ServiceException.class);
        mockMvc.perform(
                post("/subjects/update/{id}", 1L)
                        .flashAttr("subject", subject))
                .andExpect(flash().attribute("newSubject", subject))
                .andExpect(flash().attribute("subject", new Subject()))
                .andExpect(MockMvcResultMatchers.flash().attribute(
                        "serviceExceptionOnUpdate",
                        Matchers.isA(ServiceException.class)))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(subject);
    }

    @Test
    public void shouldDeleteSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", null);
        doNothing().when(subjectService).deleteSubjectById(1L);
        mockMvc.perform(
                post("/subjects/delete/{id}", 1L)
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).deleteSubjectById(1L);
    }
}
