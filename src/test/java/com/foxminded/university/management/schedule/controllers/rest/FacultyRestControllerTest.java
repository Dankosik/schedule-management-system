package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
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
@WebMvcTest(value = FacultyRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class FacultyRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyServiceImpl facultyService;

    @Test
    public void shouldReturnFacultyById() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", Collections.emptyList(), Collections.emptyList());

        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/faculties/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"name\":\"FAIT\",\"id\":1,\"groups\":[],\"teachers\":[]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(facultyService, times(1)).getFacultyById(1L);
    }

    @Test
    public void shouldReturnAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(new Faculty(1L, "FAIT", Collections.emptyList(), Collections.emptyList()),
                new Faculty(2L, "QEWQ", Collections.emptyList(), Collections.emptyList()));

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/faculties")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"name\":\"FAIT\",\"id\":1}," +
                "{\"name\":\"QEWQ\",\"id\":2}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(facultyService, times(1)).getAllFaculties();
    }

    @Test
    public void shouldCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("FAIT", null, null);

        when(facultyService.saveFaculty(faculty))
                .thenReturn(new Faculty(1L, "FAIT", Collections.emptyList(), Collections.emptyList()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/faculties")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.saveFaculty(faculty)).thenReturn(faculty);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/faculties/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\",\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(facultyService, times(1)).isFacultyWithIdExist(1L);
        verify(facultyService, times(1)).saveFaculty(faculty);
    }

    @Test
    public void shouldDeleteFaculty() throws Exception {
        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        doNothing().when(facultyService).deleteFacultyById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/faculties/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(facultyService, times(1)).deleteFacultyById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfFacultyNotExistOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/faculties/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\",\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Faculty with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(facultyService, never()).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/faculties/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\",\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(facultyService, never()).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/faculties/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\",\"id\":1,\"abc\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(facultyService, never()).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddFaculty() throws Exception {
        Faculty faculty = new Faculty("FAIT", null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/faculties")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\",\"abc\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(facultyService, never()).saveFaculty(faculty);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "FAIT", null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/faculties/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"FAIT\"}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(facultyService, never()).saveFaculty(faculty);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddFaculty() throws Exception {
        Faculty faculty = new Faculty("FAIT", null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/faculties")
                .accept(MediaType.APPLICATION_JSON)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(facultyService, never()).saveFaculty(faculty);
    }
}
