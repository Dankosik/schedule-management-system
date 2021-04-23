package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.student.StudentAddDto;
import com.foxminded.university.management.schedule.dto.student.StudentUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StudentDtoUtils.class})
class StudentDtoUtilsTest {
    @MockBean
    private GroupServiceImpl groupService;

    @Test
    void shouldReturnTrueIfSuchGroupFromStudentAddDtoExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null),
                null, null));
        assertTrue(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentAddDto("John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchGroupFromStudentAddDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null),
                null, null));
        assertFalse(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentAddDto("John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AA-22", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnTrueIfSuchGroupFromStudentUpdateDtoExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null),
                null, null));
        assertTrue(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentUpdateDto(1L, "John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchGroupFromStudentUpdateDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentUpdateDto(1L, "John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AA-22", new FacultyUpdateDto(1L, "FAIT")))));
    }

    @Test
    void shouldReturnFalseIfSuchFacultyFromStudentUpdateDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentUpdateDto(1L, "John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAITASD")))));
    }

    @Test
    void shouldReturnFalseIfSuchFacultyFromStudentAddDtoNotExist() {
        when(groupService.getGroupById(1L)).thenReturn(new Group(1L, "AB-01", new Faculty(1L, "FAIT",
                null, null), null, null));
        assertFalse(StudentDtoUtils.isSuchGroupFromStudentDtoExist(new StudentAddDto("John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAITASDAD")))));
    }

    @Test
    void shouldReturnStudentFromStudentUpdateDto() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1,
                new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null));
        StudentUpdateDto studentUpdateDto = new StudentUpdateDto(1L, "John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")));
        assertEquals(expected, StudentDtoUtils.mapStudentDtoOnStudent(studentUpdateDto));
    }

    @Test
    void shouldReturnStudentFromStudentAddDto() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1,
                new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null));
        StudentAddDto studentAddDto = new StudentAddDto("John", "Jackson",
                "Jackson", 1, new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT")));
        assertEquals(expected, StudentDtoUtils.mapStudentDtoOnStudent(studentAddDto));
    }
}
