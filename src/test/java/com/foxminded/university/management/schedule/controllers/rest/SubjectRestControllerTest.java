package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SubjectRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubjectRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectServiceImpl subjectService;

    @Test
    public void shouldReturnSubjectById() throws Exception {
        Subject subject = new Subject(1L, "Math", Collections.emptyList());

        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/subjects/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":1,\"name\":\"Math\",lessons:[]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(subjectService, times(1)).getSubjectById(1L);
    }

    @Test
    public void shouldReturnAllSubjects() throws Exception {
        List<Subject> subjects = Arrays.asList(new Subject(1L, "Math", Collections.emptyList()),
                new Subject(2L, "Programming", Collections.emptyList()),
                new Subject(3L, "Art", Collections.emptyList()));

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/subjects")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":1,\"name\":\"Math\"},{\"id\":2,\"name\":\"Programming\"},{\"id\":3,\"name\":\"Art\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(subjectService, times(1)).getAllSubjects();
    }

    @Test
    public void shouldCreateSubject() throws Exception {
        Subject subject = new Subject("Math", null);

        when(subjectService.saveSubject(subject)).thenReturn(new Subject(1L, "Math", Collections.emptyList()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/subjects")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(subjectService, times(1)).saveSubject(subject);
    }

    @Test
    public void shouldUpdateSubject() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.saveSubject(subject)).thenReturn(subject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/subjects/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(subjectService, times(1)).isSubjectWithIdExist(1L);
        verify(subjectService, times(1)).saveSubject(subject);
    }

    @Test
    public void shouldDeleteSubject() throws Exception {
        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        doNothing().when(subjectService).deleteSubjectById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/subjects/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        verify(subjectService, times(1)).deleteSubjectById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfSubjectNotExistOnUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/subjects/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Subject with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(subjectService, never()).saveSubject(subject);
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/subjects/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(subjectService, never()).saveSubject(subject);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/subjects/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"abc\":1,\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(subjectService, never()).saveSubject(subject);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddSubject() throws Exception {
        Subject subject = new Subject("Math", null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/subjects")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(subjectService, never()).saveSubject(subject);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateSubject() throws Exception {
        Subject subject = new Subject(1L, "Math", null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/subjects/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Math\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(subjectService, never()).saveSubject(subject);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddSubject() throws Exception {
        Subject subject = new Subject("Math", null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/subjects")
                .accept(MediaType.APPLICATION_JSON)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(subjectService, never()).saveSubject(subject);
    }
}
