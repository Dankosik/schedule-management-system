package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherAddDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.FacultyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TeacherDtoUtils.class})
class TeacherDtoUtilsTest {
    @MockBean
    private FacultyService facultyService;

    @Test
    void shouldReturnTeacherFromTeacherUpdateDto() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "FAIT", null, null));
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null);
        TeacherAddDto teacherAddDto = new TeacherAddDto("John", "Jackson",
                "Jackson", new FacultyUpdateDto(1L, "FAIT"));
        assertEquals(expected, TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherAddDto));
    }

    @Test
    void shouldReturnTeacherFromTeacherAddDto() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "FAIT", null, null));
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson",
                new Faculty(1L, "FAIT", null, null), null);
        TeacherUpdateDto teacherUpdateDto = new TeacherUpdateDto(1L, "John", "Jackson",
                "Jackson", new FacultyUpdateDto(1L, "FAIT"));
        assertEquals(expected, TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherUpdateDto));
    }
}
