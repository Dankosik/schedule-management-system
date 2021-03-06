package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.lesson.LessonAddDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.SubjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {LessonDtoUtils.class})
class LessonDtoUtilsTest {
    @MockBean
    private SubjectService subjectService;

    @Test
    void shouldReturnLessonFromLessonUpdateDto() {
        when(subjectService.getSubjectById(1L)).thenReturn(new Subject(1L, "Math", null));
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null);
        LessonUpdateDto lessonUpdateDto = new LessonUpdateDto(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math"));
        assertEquals(expected, LessonDtoUtils.mapLessonDtoOnLesson(lessonUpdateDto));
    }

    @Test
    void shouldReturnGroupFromGroupAddDto() {
        when(subjectService.getSubjectById(1L)).thenReturn(new Subject(1L, "Math", null));
        Lesson expected = new Lesson(1L, 1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new Subject(1L, "Math", null), null);
        LessonAddDto lessonAddDto = new LessonAddDto(1, Time.valueOf(LocalTime.of(8, 30, 0)),
                Duration.ofMinutes(90), new SubjectUpdateDto(1L, "Math"));
        assertEquals(expected, LessonDtoUtils.mapLessonDtoOnLesson(lessonAddDto));
    }
}
