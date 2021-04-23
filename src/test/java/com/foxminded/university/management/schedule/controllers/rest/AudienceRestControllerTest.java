package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
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
@WebMvcTest(value = AudienceRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class AudienceRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AudienceServiceImpl audienceService;

    @Test
    public void getAudienceById() throws Exception {
        Audience audience = new Audience(1L, 1, 1, Collections.emptyList());

        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{id: 1," +
                "number: 1," +
                "capacity: 1," +
                "lectures:[]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(audienceService, times(1)).getAudienceById(1L);
    }

    @Test
    public void getAllAudiences() throws Exception {
        List<Audience> audiences = Arrays.asList(new Audience(1L, 1, 1, Collections.emptyList()),
                new Audience(2L, 2, 2, Collections.emptyList()),
                new Audience(3L, 3, 3, Collections.emptyList()));

        when(audienceService.getAllAudiences()).thenReturn(audiences);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/audiences")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{number: 1,capacity: 1,id: 1},{number: 2,capacity: 2,id: 2},{number: 3,capacity: 3,id: 3}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(audienceService, times(1)).getAllAudiences();
    }

    @Test
    public void shouldCreateAudience() throws Exception {
        Audience audience = new Audience(1, 1, null);

        when(audienceService.saveAudience(audience)).thenReturn(new Audience(1L, 1, 1, Collections.emptyList()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/audiences")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.saveAudience(audience)).thenReturn(audience);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(audienceService, times(1)).isAudienceWithIdExist(1L);
        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldDeleteAudience() throws Exception {
        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        doNothing().when(audienceService).deleteAudienceById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/audiences/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        verify(audienceService, times(1)).deleteAudienceById(1L);
    }

    @Test
    public void shouldReturnErrorResponseOnGetAudienceById() throws Exception {
        when(audienceService.getAudienceById(1L)).thenThrow(ServiceException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Audience with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(audienceService, times(1)).getAudienceById(1L);
    }

    @Test
    public void shouldReturnErrorResponseOnDeleteAudienceById() throws Exception {
        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Audience with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(audienceService, never()).deleteAudienceById(1L);
    }

    @Test
    public void shouldReturnErrorResponseOnCreateAudience() throws Exception {
        Audience audience = new Audience(1, 1, null);

        when(audienceService.saveAudience(audience)).thenThrow(ServiceException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/audiences")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"Audience with number: 1 is already exist\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseIfNumberAlreadyExistOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        when(audienceService.saveAudience(audience)).thenThrow(ServiceException.class);
        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"Audience with number: 1 is already exist\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(audienceService, times(1)).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseIfAudienceNotExistOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Audience with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(audienceService, never()).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(audienceService, never()).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"id\":1,\"abc\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(audienceService, never()).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddAudience() throws Exception {
        Audience audience = new Audience(1, 1, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/audiences")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1,\"abc\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(audienceService, never()).saveAudience(audience);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateAudience() throws Exception {
        Audience audience = new Audience(1L, 1, 1, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/audiences/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"capacity\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(audienceService, never()).saveAudience(audience);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddAudience() throws Exception {
        Audience audience = new Audience(1, 1, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/audiences")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"capacity\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(audienceService, never()).saveAudience(audience);
    }
}
