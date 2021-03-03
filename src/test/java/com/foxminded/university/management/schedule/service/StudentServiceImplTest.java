package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {
    private final Student student = new Student(1L, "John", "Jackson", "Jackson", 1, null);
    private final List<Student> students = List.of(student,
            new Student(2L, "Ferdinanda", "Casajuana", "Lambarton", 1, null),
            new Student(3L, "Lindsey", "Syplus", "Slocket", 1, null));
    @Autowired
    private StudentServiceImpl studentService;
    @MockBean
    private StudentDao studentDao;
    @MockBean
    private GroupDao groupDao;

    @Test
    void shouldSaveStudent() {
        when(studentDao.save(new Student("John", "Jackson", "Jackson", 1, null)))
                .thenReturn(student);
        Student actual = studentService.saveStudent(student);

        assertEquals(student, actual);

        verify(studentDao, times(1)).save(student);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));

        Student actual = studentService.getStudentById(1L);

        assertEquals(student, actual);

        verify(studentDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfStudents() {
        when(studentDao.getAll()).thenReturn(students);

        assertEquals(students, studentService.getAllStudents());

        verify(studentDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));
        when(studentDao.deleteById(1L)).thenReturn(true);

        studentService.deleteStudentById(1L);

        verify(studentDao, times(1)).deleteById(1L);
        verify(studentDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfStudents() {
        when(studentDao.save(new Student("John", "Jackson", "Jackson", 1, null)))
                .thenReturn(student);
        when(studentDao.save(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, null)))
                .thenReturn(students.get(1));
        when(studentDao.save(new Student("Lindsey", "Syplus", "Slocket", 1, null)))
                .thenReturn(students.get(2));

        List<Student> actual = studentService.saveAllStudents(students);

        assertEquals(students, actual);

        verify(studentDao, times(1)).save(students.get(0));
        verify(studentDao, times(1)).save(students.get(1));
        verify(studentDao, times(1)).save(students.get(2));
    }

    @Test
    void shouldThrowExceptionIfStudentWithInputIdNotFound() {
        when(studentDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> studentService.getStudentById(1L));

        verify(studentDao, times(1)).getById(1L);
        verify(studentDao, never()).save(student);
    }

    @Test
    void shouldThrowExceptionIfStudentGroupNotFound() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 1L);

        when(studentDao.getById(1L)).thenReturn(Optional.of(expected));
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> studentService.saveStudent(expected));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, never()).save(expected);
    }
}
