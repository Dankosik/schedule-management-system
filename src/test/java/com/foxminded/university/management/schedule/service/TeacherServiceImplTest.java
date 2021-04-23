package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.repository.FacultyRepository;
import com.foxminded.university.management.schedule.repository.TeacherRepository;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {
    private final Faculty faculty = new Faculty(1L, "FAIT", null, null);
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);
    private final List<Teacher> teachers = List.of(teacher,
            new Teacher(2L, "Mike", "Conor", "Conor", faculty, null));
    @Autowired
    TeacherServiceImpl teacherService;
    @MockBean
    private TeacherRepository teacherRepository;
    @MockBean
    private FacultyRepository facultyRepository;


    @Test
    void shouldSaveTeacher() {
        when(teacherRepository.save(new Teacher("John", "Jackson", "Jackson", faculty, null)))
                .thenReturn(teacher);
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        Teacher actual = teacherService.saveTeacher(teacher);

        assertEquals(teacher, actual);

        verify(teacherRepository, times(1)).save(teacher);
        verify(facultyRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnTeacherWithIdOne() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher actual = teacherService.getTeacherById(1L);

        assertEquals(teacher, actual);

        verify(teacherRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfTeachers() {
        when(teacherRepository.findAll()).thenReturn(teachers);

        assertEquals(teachers, teacherService.getAllTeachers());

        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        teacherService.deleteTeacherById(1L);

        verify(teacherRepository, times(1)).deleteById(1L);
        verify(teacherRepository, times(3)).findById(1L);
    }

    @Test
    void shouldSaveListOfTeachers() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        when(teacherRepository.save(new Teacher("John", "Jackson", "Jackson", faculty, null)))
                .thenReturn(teacher);
        when(teacherRepository.save(new Teacher("Mike", "Conor", "Conor", faculty, null)))
                .thenReturn(teachers.get(1));

        List<Teacher> actual = teacherService.saveAllTeachers(teachers);

        assertEquals(teachers, actual);

        verify(teacherRepository, times(1)).save(teachers.get(0));
        verify(teacherRepository, times(1)).save(teachers.get(1));
        verify(facultyRepository, times(2)).findById(1L);
    }

    @Test
    void shouldThrowExceptionIfStudentWithInputIdNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teacherService.getTeacherById(1L));

        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).save(teacher);
    }

    @Test
    void shouldThrowExceptionIfTeachersFacultyNotFound() {
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> teacherService.saveTeacher(expected));

        verify(facultyRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).save(expected);
    }

    @Test
    void shouldReturnNameWithInitialsForTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null));

        List<String> expected = List.of("Jackson J. J.", "Conor M. C.");

        assertEquals(expected, teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers));
    }

    @Test
    void shouldReturnNameWithInitialsForTeachersWithTeacherIdNull() {
        List<Teacher> teachers = Arrays.asList(null, new Teacher(2L, "Mike", "Conor", "Conor", null, null));

        List<String> expected = Arrays.asList(null, "Conor M. C.");

        assertEquals(expected, teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers));
    }

    @Test
    void shouldReturnTeachersForLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null,
                        new Teacher(1L, "John", "Jackson", "Jackson", null, null)),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null,
                        new Teacher(2L, "Mike", "Conor", "Conor", null, null)));

        List<Teacher> expected = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", null, null),
                new Teacher(2L, "Mike", "Conor", "Conor", null, null));

        assertEquals(expected, teacherService.getTeachersWithPossibleNullForLectures(lectures));
    }

    @Test
    void shouldReturnTeachersForLecturesWithTeacherIdZero() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null,
                        new Teacher(2L, "Mike", "Conor", "Conor", null, null)));

        List<Teacher> expected = Arrays.asList(null, new Teacher(2L, "Mike", "Conor", "Conor", null, null));

        assertEquals(expected, teacherService.getTeachersWithPossibleNullForLectures(lectures));
    }

    @Test
    void shouldReturnTrueIfTeacherWithIdExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(new Teacher(2L, "Mike", "Conor",
                "Conor", null, null)));
        assertTrue(teacherService.isTeacherWithIdExist(1L));
    }

    @Test
    void shouldReturnFalseIfTeacherWithIdNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(teacherService.isTeacherWithIdExist(1L));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfTeacherNotExistOnDelete() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> teacherService.deleteTeacherById(1L));

        verify(teacherRepository, times(1)).findById(1L);
        verify(teacherRepository, never()).deleteById(1L);
    }
}
