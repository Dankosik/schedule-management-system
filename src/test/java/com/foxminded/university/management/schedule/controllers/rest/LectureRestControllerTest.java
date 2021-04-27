package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.lecture.LectureUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LectureDtoUtils;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LectureRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class LectureRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LectureServiceImpl lectureService;
    @MockBean
    private AudienceServiceImpl audienceService;
    @MockBean
    private TeacherServiceImpl teacherService;
    @MockBean
    private GroupServiceImpl groupService;
    @MockBean
    private LessonServiceImpl lessonService;
    @MockBean
    private LectureDtoUtils lectureDtoUtils;

    @BeforeEach
    void init() {
        lectureDtoUtils = new LectureDtoUtils(lessonService, groupService, teacherService, audienceService);
    }

    @Test
    public void shouldReturnLectureById() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);
        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                null, group, null, null);

        when(lectureService.getLectureById(1L)).thenReturn(lecture);

        RequestBuilder requestBuilder = get("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"id\":1,\"number\":1,\"date\":\"2021-01-01\"," +
                "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                "\"lesson\":null, \"teacher\":null}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(lectureService, times(1)).getLectureById(1L);
    }

    @Test
    public void shouldReturnAllLectures() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, group, null, null),
                new Lecture(2L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, group, null, null),
                new Lecture(3L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, group, null, null));

        when(lectureService.getAllLectures()).thenReturn(lectures);

        RequestBuilder requestBuilder = get("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "[{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":null,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}},\"lesson\":null,\"teacher\":null}" +
                ",{\"id\":2,\"number\":1,\"date\":\"2021-01-01\",\"audience\":null,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}},\"lesson\":null,\"teacher\":null}," +
                "{\"id\":3,\"number\":1,\"date\":\"2021-01-01\",\"audience\":null,\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}},\"lesson\":null,\"teacher\":null}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(lectureService, times(1)).getAllLectures();
    }

    @Test
    public void shouldCreateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);


        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    public void shouldUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        RequestBuilder requestBuilder = put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    public void shouldDeleteLecture() throws Exception {
        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        doNothing().when(lectureService).deleteLectureById(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/v1/lectures/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        verify(lectureService, times(1)).deleteLectureById(1L);
    }

    @Test
    public void shouldReturnErrorResponseIfLectureNotFoundOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(false);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        RequestBuilder requestBuilder = put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{\"error\":\"NOT_FOUND\",\"message\":\"Lecture with id: 1 is not found\",\"status\":404}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesAudienceNotFoundOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(false);
        when(audienceService.getAudienceById(1L)).thenThrow(EntityNotFoundException.class);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        mockMvc.perform(put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesTeacherNotFoundOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(false);
        when(teacherService.getTeacherById(1L)).thenThrow(EntityNotFoundException.class);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        mockMvc.perform(put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));
        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesGroupNotFoundOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(false);
        when(groupService.getGroupById(1L)).thenThrow(EntityNotFoundException.class);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        mockMvc.perform(put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesGroupNotFoundOnAddLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(false);
        when(groupService.getGroupById(1L)).thenThrow(EntityNotFoundException.class);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        mockMvc.perform(post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesLessonNotFoundOnAddLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(false);
        when(lessonService.getLessonById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLectureTeacherNotFoundOnAddLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(false);
        when(teacherService.getTeacherById(1L)).thenThrow(EntityNotFoundException.class);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesLessonNotFoundOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(false);
        when(lessonService.getLessonById(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/v1/lectures/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesAudienceNotExistOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(true);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        try (MockedStatic<LectureDtoUtils> lectureDtoUtilsMockedStatic = mockStatic(LectureDtoUtils.class)) {
            lectureDtoUtilsMockedStatic.when(() -> LectureDtoUtils.mapLectureDtoOnLecture(any(LectureUpdateDto.class)))
                    .thenThrow(EntityNotFoundException.class);

            mockMvc.perform(put("/api/v1/lectures/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                            "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                            "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                            "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                            "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                            "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

            verify(lessonService, never()).saveLesson(lesson);
        }
    }

    @Test
    public void shouldReturnErrorResponseIfLecturesAudienceNotFoundOnAddLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        when(lectureService.isLectureWithIdExist(1L)).thenReturn(true);
        when(lectureService.saveLecture(lecture)).thenReturn(new Lecture(1L, 1,
                Date.valueOf(LocalDate.of(2021, 1, 1)), audience, group, lesson, teacher));

        when(audienceService.isAudienceWithIdExist(1L)).thenReturn(false);
        when(audienceService.getAudienceById(1L)).thenThrow(EntityNotFoundException.class);

        when(teacherService.isTeacherWithIdExist(1L)).thenReturn(true);
        when(teacherService.getTeacherById(1L)).thenReturn(teacher);

        when(groupService.isGroupWithIdExist(1L)).thenReturn(true);
        when(groupService.getGroupById(1L)).thenReturn(group);

        when(lessonService.isLessonWithIdExist(1L)).thenReturn(true);
        when(lessonService.getLessonById(1L)).thenReturn(lesson);

        mockMvc.perform(post("/api/v1/lectures")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(EntityNotFoundException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lessonService, never()).saveLesson(lesson);
    }


    @Test
    public void shouldReturnErrorResponseURIAndIdNotSameOnUpdateLecture() throws Exception {
        Group group = new Group();
        group.setId(1L);
        group.setName("AB-01");
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("FAIT");
        group.setFaculty(faculty);

        Audience audience = new Audience(1L, 1, 1, null);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");

        Lesson lesson = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)), Duration.ofMinutes(90), subject, null);

        Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                audience, group, lesson, teacher);

        mockMvc.perform(put("/api/v1/lectures/2")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"number\":1,\"date\":\"2021-01-01\",\"audience\":{\"number\":1,\"capacity\":1,\"id\":1}," +
                        "\"group\":{\"id\":1,\"name\":\"AB-01\",\"faculty\":{\"name\":\"FAIT\",\"id\":1}}," +
                        "\"lesson\":{\"id\":1,\"number\":1,\"startTime\":\"08:30:00\",\"duration\":\"PT1H30M\"," +
                        "\"subject\":{\"id\":1,\"name\":\"Math\"}}," +
                        "\"teacher\":{ \"firstName\":\"John\",\"lastName\":\"Jackson\",\"middleName\":\"Jackson\",\"id\":1," +
                        "\"faculty\":{\"name\":\"FAIT\",\"id\":1}}}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(a -> assertEquals(UnacceptableUriException.class, Objects.requireNonNull(a.getResolvedException()).getClass()));

        verify(lectureService, never()).saveLecture(lecture);
    }
}
