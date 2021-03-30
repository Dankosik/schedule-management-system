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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private TeacherDao teacherDao;
    @MockBean
    private FacultyDao facultyDao;


    @Test
    void shouldSaveTeacher() {
        when(teacherDao.save(new Teacher("John", "Jackson", "Jackson", faculty, null)))
                .thenReturn(teacher);
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));

        Teacher actual = teacherService.saveTeacher(teacher);

        assertEquals(teacher, actual);

        verify(teacherDao, times(1)).save(teacher);
        verify(facultyDao, times(1)).getById(1L);
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
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(teacherDao.save(new Teacher("John", "Jackson", "Jackson", faculty, null)))
                .thenReturn(teacher);
        when(teacherDao.save(new Teacher("Mike", "Conor", "Conor", faculty, null)))
                .thenReturn(teachers.get(1));

        List<Teacher> actual = teacherService.saveAllTeachers(teachers);

        assertEquals(teachers, actual);

        verify(teacherDao, times(1)).save(teachers.get(0));
        verify(teacherDao, times(1)).save(teachers.get(1));
        verify(facultyDao, times(2)).getById(1L);
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
        Teacher expected = new Teacher(1L, "John", "Jackson", "Jackson", faculty, null);

        when(teacherDao.getById(1L)).thenReturn(Optional.of(expected));
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> teacherService.saveTeacher(expected));

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, never()).save(expected);
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
}
