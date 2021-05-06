package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.student.StudentAddDto;
import com.foxminded.university.management.schedule.dto.utils.StudentDtoUtils;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.StudentService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
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
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StudentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupService groupService;
    @MockBean
    private StudentService studentService;
    @Mock
    private StudentDtoUtils studentDtoUtils;
    private Group group;

    @BeforeEach
    void init() {
        studentDtoUtils = new StudentDtoUtils(groupService);
        group = new Group();
        group.setName("AB-01");
        group.setId(1L);
        Faculty faculty = new Faculty();
        faculty.setName("FAIT");
        faculty.setId(1L);
        group.setFaculty(faculty);
    }

    @Test
    public void shouldReturnStudentById() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        when(studentService.getStudentById(1L)).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/students/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"id\":1,\"courseNumber\":1,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    public void shouldReturnAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "Mary", "Taylor", "Garcia", 1, group),
                new Student(2L, "Linda", "Jones", "Martinez", 1, group),
                new Student(3L, "William", "Taylor", "Johns", 1, group));

        when(studentService.getAllStudents()).thenReturn(students);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"id\":1,\"courseNumber\":1,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}," +
                "{\"firstName\":\"Linda\",\"lastName\":\"Jones\",\"middleName\":\"Martinez\",\"id\":2,\"courseNumber\":1,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}," +
                "{\"firstName\":\"William\",\"lastName\":\"Taylor\",\"middleName\":\"Johns\",\"id\":3,\"courseNumber\":1,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    public void shouldCreateStudent() throws Exception {
        Student student = new Student("Mary", "Taylor", "Garcia", 1, group);

        when(studentService.saveStudent(student))
                .thenReturn(new Student(1L, "Mary", "Taylor", "Garcia", 1, group));
        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    public void shouldUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(studentService.isStudentWithIdExist(1L)).thenReturn(true);
        when(studentService.saveStudent(student)).thenReturn(student);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/students/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(studentService, times(1)).isStudentWithIdExist(1L);
        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    public void shouldDeleteStudent() throws Exception {
        when(studentService.isStudentWithIdExist(1L)).thenReturn(true);
        doNothing().when(studentService).deleteStudentById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/students/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(studentService, times(1)).deleteStudentById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentNotFoundOnUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);
        when(studentService.isStudentWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/students/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Student with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentsGroupNotFoundOnUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        when(groupService.getGroupById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/students/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(studentService, never()).saveStudent(student);
    }


    @Test
    public void shouldReturnErrorResponseIfStudentsGroupNotFoundOnAddStudent() throws Exception {
        Student student = new Student("Mary", "Taylor", "Garcia", 1, group);

        when(groupService.getGroupById(1L)).thenThrow(EntityNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldReturnErrorResponseIfStudentsGroupNotExistOnAddStudent() throws Exception {
        Student student = new Student("Mary", "Taylor", "Garcia", 1, group);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(false);
        when(groupService.getGroupById(1L)).thenThrow(EntityNotFoundException.class);
        StudentAddDto studentAddDto = new StudentAddDto("Mary", "Taylor", "Garcia",
                1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")));
        try (MockedStatic<StudentDtoUtils> studentDtoUtilsMockedStatic = mockStatic(StudentDtoUtils.class)) {
            studentDtoUtilsMockedStatic.when(() -> StudentDtoUtils.mapStudentDtoOnStudent(any(StudentAddDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(post("/api/v1/students")
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                            "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

            verify(studentService, never()).saveStudent(student);
        }
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/students/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/students/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"id\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(studentService, never()).saveStudent(student);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/students/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\":\"Mary\",\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(studentService, never()).saveStudent(student);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddStudent() throws Exception {
        Student student = new Student(1L, "Mary", "Taylor", "Garcia", 1, group);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/students")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"lastName\":\"Taylor\",\"middleName\":\"Garcia\",\"courseNumber\":1," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(studentService, never()).saveStudent(student);
    }
}
