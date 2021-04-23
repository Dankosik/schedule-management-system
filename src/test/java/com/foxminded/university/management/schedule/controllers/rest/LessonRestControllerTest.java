package com.foxminded.university.management.schedule.controllers.rest;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.foxminded.university.management.schedule.dto.lesson.LessonAddDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LessonDtoUtils;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
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

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LessonRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubjectServiceImpl subjectService;
    @MockBean
    private LessonServiceImpl lessonService;
    @Mock
    private LessonDtoUtils lessonDtoUtils;

    @BeforeEach
    void init() {
        lessonDtoUtils = new LessonDtoUtils(subjectService);
    }

    @Test
    public void getLessonById() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, Collections.emptyList());

        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    public void shouldReturnErrorResponseOnGetLessonById() throws Exception {
        when(lessonService.getLessonById(1L)).thenThrow(ServiceException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Lesson with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, times(1)).getLessonById(1L);
    }

    @Test
    public void getAllLessons() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        List<Lesson> lessons = Arrays.asList(
                new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                        Duration.ofMinutes(90), subject, Collections.emptyList()),
                new Lesson(2L, 2, Time.valueOf(LocalTime.of(9, 30, 0)),
                        Duration.ofMinutes(90), subject, Collections.emptyList()),
                new Lesson(3L, 3, Time.valueOf(LocalTime.of(10, 30, 0)),
                        Duration.ofMinutes(90), subject, Collections.emptyList()));

        when(lessonService.getAllLessons()).thenReturn(lessons);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/lessons")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}" +
                ",{\"id\":2,\"number\":2,\"startTime\":\"09:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                "{\"id\":3,\"number\":3,\"startTime\":\"10:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(lessonService, times(1)).getAllLessons();
    }

    @Test
    public void shouldCreateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(lessonService.saveLesson(lesson)).thenReturn(new Lesson(1L, 1,
                Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null));
        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        verify(lessonService, times(1)).saveLesson(lesson);
    }

    @Test
    public void shouldUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.saveLesson(lesson)).thenReturn(lesson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(lessonService, times(1)).isLessonWithIdExist(1L);
        verify(lessonService, times(1)).saveLesson(lesson);
    }

    @Test
    public void shouldDeleteLesson() throws Exception {
        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        doNothing().when(lessonService).deleteLessonById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/lessons/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        verify(lessonService, times(1)).deleteLessonById(1L);
    }

    @Test
    public void shouldReturnErrorResponseOnDeleteLessonById() throws Exception {
        when(lessonService.isLessonWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Lesson with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, never()).deleteLessonById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfLessonNotFoundOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        when(lessonService.isLessonWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Lesson with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLessonsSubjectNotFoundOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Subject with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLessonsSubjectNotExistOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);
        LessonUpdateDto lessonUpdateDto = new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math"));
        try (MockedStatic<LessonDtoUtils> lessonDtoUtilsMockedStatic = mockStatic(LessonDtoUtils.class)) {
            lessonDtoUtilsMockedStatic.when(() -> LessonDtoUtils.isSuchSubjectFromLessonDtoExist(lessonUpdateDto)).thenReturn(false);

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .put("/api/v1/lessons/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                    .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Such subject does not exist\",\"status\":404}";

            JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

            verify(lessonService, never()).saveLesson(lesson);
        }
    }

    @Test
    public void shouldReturnErrorResponseIfLessonsFacultyNotFoundOnAddLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Subject with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLessonsSubjectNotExistOnAddLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        when(subjectService.isSubjectWithIdExist(1L)).thenReturn(true);
        when(subjectService.getSubjectById(1L)).thenReturn(subject);

        LessonAddDto lessonAddDto = new LessonAddDto(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math"));
        try (MockedStatic<LessonDtoUtils> lessonDtoUtilsMockedStatic = mockStatic(LessonDtoUtils.class)) {
            lessonDtoUtilsMockedStatic.when(() -> LessonDtoUtils.isSuchSubjectFromLessonDtoExist(lessonAddDto)).thenReturn(false);

            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post("/api/v1/lessons")
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                    .contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Such subject does not exist\",\"status\":404}";

            JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
            assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

            verify(lessonService, never()).saveLesson(lesson);
        }
    }

    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"BAD_REQUEST\",\"message\":\"URI id: 2 and request id: 1 should be the same\",\"status\":400}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseOnUnrecognizedFieldOnAddLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"abc\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(UnrecognizedPropertyException.class, cause.getClass());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnUpdateLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/v1/lessons/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(lessonService, never()).saveLesson(lesson);
    }


    @Test
    public void shouldReturnErrorResponseOnMismatchedInputExceptionOnAddLesson() throws Exception {
        Subject subject = new Subject();
        subject.setName("Math");
        subject.setId(1L);
        Lesson lesson = new Lesson(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), subject, null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lessons")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\",\"subject\":{\"id\":1,\"name\":\"Math\"}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Throwable cause = result.getResolvedException().getCause();
        assertEquals(MismatchedInputException.class, cause.getClass());

        verify(lessonService, never()).saveLesson(lesson);
    }
}
