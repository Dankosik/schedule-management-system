package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TeacherServiceImplTest {
    private final Teacher teacher = new Teacher(1L, "John", "Jackson", "Jackson", null, 1L);
    private final List<Teacher> teachers = List.of(teacher,
            new Teacher(2L, "Mike", "Conor", "Conor", 1L, 1L));
    @Autowired
    TeacherServiceImpl teacherService;
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private FacultyDao facultyDao;


    @Test
    void shouldSaveTeacher() {
        when(teacherDao.save(new Teacher("John", "Jackson", "Jackson", null, 1L)))
                .thenReturn(teacher);
        Teacher actual = teacherService.saveTeacher(teacher);

        assertEquals(teacher, actual);

        verify(teacherDao, times(1)).save(teacher);
    }

    @Test
    void shouldReturnTeacherWithIdOne() {
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        Teacher actual = teacherService.getTeacherById(1L);

        assertEquals(teacher, actual);

        verify(teacherDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfTeachers() {
        when(teacherDao.getAll()).thenReturn(teachers);

        assertEquals(teachers, teacherService.getAllTeachers());

        verify(teacherDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(teacherDao.deleteById(1L)).thenReturn(true);

        teacherService.deleteTeacherById(1L);

        verify(teacherDao, times(1)).deleteById(1L);
        verify(teacherDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfTeachers() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", 1L)));
        when(teacherDao.save(new Teacher("John", "Jackson", "Jackson", null, 1L)))
                .thenReturn(teacher);
        when(teacherDao.save(new Teacher("Mike", "Conor", "Conor", 1L, 1L)))
                .thenReturn(teachers.get(1));

        List<Teacher> actual = teacherService.saveAllTeachers(teachers);

        assertEquals(teachers, actual);

        verify(teacherDao, times(1)).save(teachers.get(0));
        verify(teacherDao, times(1)).save(teachers.get(1));
        verify(facultyDao, times(1)).getById(1L);
    }

    @Test
    void shouldThrowExceptionIfStudentWithInputIdNotFound() {
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> teacherService.getTeacherById(1L));

        verify(teacherDao, times(1)).getById(1L);
        verify(teacherDao, never()).save(teacher);
    }

    @Test
    void shouldThrowExceptionIfTeachersFacultyNotFound() {
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L);

        when(teacherDao.getById(1L)).thenReturn(Optional.of(expected));
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> teacherService.saveTeacher(expected));

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, never()).save(expected);
    }

    @Test
    void shouldReturnNameWithInitialsForTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L));

        List<String> expected = List.of("Jackson J. J.", "Conor M. C.");

        assertEquals(expected, teacherService.getLastNamesWithInitialsForTeachers(teachers));
    }

    @Test
    void shouldReturnSubjectsForLessons() {
        when(teacherDao.getById(1L))
                .thenReturn(Optional.of(new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L)));
        when(teacherDao.getById(2L))
                .thenReturn(Optional.of(new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L)));

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 1L, 2L, 2L));

        List<Teacher> expected = List.of(
                new Teacher(1L, "John", "Jackson", "Jackson", 1L, 1L),
                new Teacher(2L, "Mike", "Conor", "Conor", 2L, 1L));

        assertEquals(expected, teacherService.getTeachersForLectures(lectures));

        verify(teacherDao, times(2)).getById(1L);
        verify(teacherDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnTeachersForFaculty() {
        List<Teacher> expected = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard", 1L, 1L),
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey", 1L, 1L));

        when(teacherDao.getTeachersByFacultyId(1L)).thenReturn(expected);

        assertEquals(expected, teacherService.getTeachersForFaculty(new Faculty(1L, "FAIT", 1L)));

        verify(teacherDao, times(1)).getTeachersByFacultyId(1L);
    }
}
