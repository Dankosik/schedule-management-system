package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
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
@WebMvcTest(SubjectController.class)
class SubjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectServiceImpl subjectService;

    @Test
    public void shouldReturnViewWithAllSubjects() throws Exception {
        List<Subject> subjects = List.of(
                new Subject(1L, "Math"),
                new Subject(2L, "Art"));

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjects));
        verify(subjectService, times(1)).getAllSubjects();
    }

    @Test
    public void shouldReturnViewWithOneSubject() throws Exception {
        Subject subject = new Subject(1L, "Math");
        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        mockMvc.perform(get("/subjects/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("subject"))
                .andExpect(model().attribute("subject", subject));
    }

    @Test
    public void shouldAddSubject() throws Exception {
        Subject subject = new Subject(1L, "Art");
        when(subjectService.saveSubject(new Subject("Art"))).thenReturn(subject);
        mockMvc.perform(
                post("/subjects/add")
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(new Subject("Art"));
    }

    @Test
    public void shouldUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Art");
        when(subjectService.saveSubject(subject)).thenReturn(subject);
        mockMvc.perform(
                post("/subjects/update/{id}", 1L)
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).saveSubject(subject);
    }

    @Test
    public void shouldDeleteSubject() throws Exception {
        Subject subject = new Subject(1L, "Art");
        doNothing().when(subjectService).deleteSubjectById(1L);
        mockMvc.perform(
                post("/subjects/delete/{id}", 1L)
                        .flashAttr("subject", subject))
                .andExpect(redirectedUrl("/subjects"))
                .andExpect(view().name("redirect:/subjects"));

        verify(subjectService, times(1)).deleteSubjectById(1L);
    }
}
