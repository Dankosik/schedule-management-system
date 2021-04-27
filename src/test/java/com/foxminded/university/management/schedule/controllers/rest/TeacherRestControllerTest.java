package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.TeacherDtoUtils;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
@WebMvcTest(value = TeacherRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TeacherServiceImpl teacherService;
    @MockBean
    private FacultyServiceImpl facultyService;
    @Mock
    private TeacherDtoUtils teacherDtoUtils;

    @BeforeEach
    void init() {
        teacherDtoUtils = new TeacherDtoUtils(facultyService);
    }

    @Test
    public void shouldReturnTeacherById() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, Collections.emptyList());

        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/teachers/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1},\"lectures\":[]}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(teacherService, times(1)).getTeacherById(1L);
    }

    @Test
    public void shouldReturnAllTeachers() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        List<Teacher> teachers = Arrays.asList(
                new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, Collections.emptyList()),
                new Teacher(2L, "Linda", "Jones", "Martinez", faculty, Collections.emptyList()),
                new Teacher(3L, "William", "Taylor", "Johns", faculty, Collections.emptyList()));

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/teachers")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"id\":1,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                "{\"firstName\":\"Linda\",\"lastName\":\"Jones\",\"middleName\":\"Martinez\",\"id\":2,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                "{\"firstName\":\"William\",\"lastName\":\"Taylor\",\"middleName\":\"Johns\",\"id\":3,\"faculty\":{\"name\":\"FAIT\",\"id\":1}}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(teacherService, times(1)).getAllTeachers();
    }

    @Test
    public void shouldCreateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher("Mary", "Taylor", "Garcia", faculty, null);

        when(teacherService.saveTeacher(teacher))
                .thenReturn(new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null));
        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/teachers")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(teacherService, times(1)).saveTeacher(teacher);
    }

    @Test
    public void shouldUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher("Mary", "Taylor", "Garcia", faculty, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);
        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.saveTeacher(teacher)).thenReturn(teacher);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/teachers/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(teacherService, times(1)).isTeacherWithIdExist(1L);
        verify(teacherService, times(1)).saveTeacher(teacher);
    }

    @Test
    public void shouldDeleteTeacher() throws Exception {
        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        doNothing().when(teacherService).deleteTeacherById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/teachers/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(teacherService, times(1)).deleteTeacherById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentNotFoundOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);
        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/teachers/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Teacher with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentsGroupNotFoundOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        when(facultyService.getFacultyById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/teachers/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentsGroupNotExistOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        when(facultyService.isFacultyWithIdExist(1L)).thenReturn(true);
        when(facultyService.getFacultyById(1L)).thenReturn(faculty);
        try (MockedStatic<TeacherDtoUtils> teacherDtoUtilsMockedStatic = mockStatic(TeacherDtoUtils.class)) {
            teacherDtoUtilsMockedStatic.when(() -> TeacherDtoUtils.mapTeacherDtoOnTeacher(any(TeacherUpdateDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(put("/api/v1/teachers/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

            verify(teacherService, never()).saveTeacher(teacher);
        }
    }

    @Test
    public void shouldReturnErrorResponseIfTeacherFacultyNotFoundOnAddTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        when(facultyService.getFacultyById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/api/v1/teachers")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/teachers/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/teachers/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"abc\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher("Mary", "Taylor", "Garcia", faculty, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/teachers")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher(1L, "Mary", "Taylor", "Garcia", faculty, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/teachers/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(teacherService, never()).saveTeacher(teacher);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddTeacher() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        Teacher teacher = new Teacher("Mary", "Taylor", "Garcia", faculty, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/teachers")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(teacherService, never()).saveTeacher(teacher);
    }
}
