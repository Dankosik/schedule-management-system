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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
                new Subject(1L, "Math", 1L),
                new Subject(2L, "Art", 1L));

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects"))
                .andExpect(model().attribute("subjects", subjects));
    }

    @Test
    public void shouldReturnViewWithOneSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", 1L);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        mockMvc.perform(get("/subjects/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("subject"))
                .andExpect(model().attribute("subject", subject));
    }

    @Test
    public void shouldDeleteSubject() throws Exception {
        Subject subject = new Subject(1L, "Art", 1L);
        given(subjectService.getSubjectById(1L)).willReturn(subject);
        doNothing().when(subjectService).deleteSubjectById(1L);
        mockMvc.perform(post("/subjects/delete/{id}", 1L))
                .andExpect(status().is3xxRedirection());
    }
}
