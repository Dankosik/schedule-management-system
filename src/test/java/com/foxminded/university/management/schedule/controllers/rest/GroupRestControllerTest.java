package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.dto.utils.GroupDtoUtils;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = GroupRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupServiceImpl groupService;
    @MockBean
    private FacultyServiceImpl facultyService;
    @Mock
    private GroupDtoUtils groupDtoUtils;

    @BeforeEach
    void init() {
        groupDtoUtils = new GroupDtoUtils(facultyService);
    }

    @Test
    public void shouldReturnGroupById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, Collections.emptyList(), Collections.emptyList());

        when(groupService.getGroupById(1L)).thenReturn(group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/groups/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1},\"students\":[],\"lectures\":[]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(groupService, times(1)).getGroupById(1L);
    }

    @Test
    public void shouldReturnAllGroups() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        List<Group> groups = Arrays.asList(new Group(1L, "AB-01", faculty, Collections.emptyList(), Collections.emptyList()),
                new Group(2L, "AB-02", faculty, Collections.emptyList(), Collections.emptyList()),
                new Group(3L, "AB-03", faculty, Collections.emptyList(), Collections.emptyList()));

        when(groupService.getAllGroups()).thenReturn(groups);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/groups")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}" +
                ",{\"id\":2,\"name\":\"AB-02\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                "{\"id\":3,\"name\":\"AB-03\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(groupService, times(1)).getAllGroups();
    }

    @Test
    public void shouldCreateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group("AB-01", faculty, null, null);

        when(groupService.saveGroup(group)).thenReturn(new Group(1L, "AB-01", faculty, null, null));
        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/groups")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    public void shouldUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);
        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.saveGroup(group)).thenReturn(group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":\"1\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(groupService, times(1)).isGroupWithIdExist(1L);
        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    public void shouldDeleteGroup() throws Exception {
        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        doNothing().when(groupService).deleteGroupById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/groups/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(groupService, times(1)).deleteGroupById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfGroupNotFoundOnUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(groupService.isGroupWithIdExist(1L)).thenReturn(false);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Group with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseIfGroupsFacultyNotFoundOnUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        when(facultyService.getFacultyById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/groups/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseIfGroupsFacultyNotFoundOnAddGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group("AB-01", faculty, null, null);

        when(facultyService.getFacultyById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/api/v1/groups")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/groups/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/groups/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1,\"abc\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group("AB-01", faculty, null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/groups")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"faculty\":{\"id\":1,\"name\":\"FAIT\",\"abc\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(groupService, never()).saveGroup(group);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group(1L, "AB-01", faculty, null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/groups/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1,\"faculty\":{\"name\":\"FAIT\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(groupService, never()).saveGroup(group);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddGroup() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Group group = new Group("AB-01", faculty, null, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/groups")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"AB-01\",\"id\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(groupService, never()).saveGroup(group);
    }
}
