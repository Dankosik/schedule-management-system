package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.repository.GroupRepository;
import com.foxminded.university.management.schedule.repository.StudentRepository;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {
    private final Group group = new Group(1L, "AB-01", null, null, null);
    private final Student student = new Student(1L, "John", "Jackson", "Jackson", 1, group);
    private final List<Student> students = List.of(student,
            new Student(2L, "Ferdinanda", "Casajuana", "Lambarton", 1, group),
            new Student(3L, "Lindsey", "Syplus", "Slocket", 1, group));
    @Autowired
    private StudentServiceImpl studentService;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void shouldSaveStudent() {
        when(studentRepository.save(new Student("John", "Jackson", "Jackson", 1, group)))
                .thenReturn(student);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        Student actual = studentService.saveStudent(student);

        assertEquals(student, actual);

        verify(studentRepository, times(1)).save(student);
        verify(groupRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnStudentWithIdOne() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student actual = studentService.getStudentById(1L);

        assertEquals(student, actual);

        verify(studentRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfStudents() {
        when(studentRepository.findAll()).thenReturn(students);

        assertEquals(students, studentService.getAllStudents());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.deleteStudentById(1L);

        verify(studentRepository, times(1)).deleteById(1L);
        verify(studentRepository, times(3)).findById(1L);
    }

    @Test
    void shouldSaveListOfStudents() {
        when(studentRepository.save(new Student("John", "Jackson", "Jackson", 1, group)))
                .thenReturn(student);
        when(studentRepository.save(new Student("Ferdinanda", "Casajuana", "Lambarton", 1, group)))
                .thenReturn(students.get(1));
        when(studentRepository.save(new Student("Lindsey", "Syplus", "Slocket", 1, group)))
                .thenReturn(students.get(2));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        List<Student> actual = studentService.saveAllStudents(students);

        assertEquals(students, actual);

        verify(studentRepository, times(1)).save(students.get(0));
        verify(studentRepository, times(1)).save(students.get(1));
        verify(studentRepository, times(1)).save(students.get(2));
        verify(groupRepository, times(3)).findById(1L);
    }

    @Test
    void shouldThrowExceptionIfStudentWithInputIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.getStudentById(1L));

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).save(student);
    }

    @Test
    void shouldThrowExceptionIfStudentGroupNotFound() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, group);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(expected));
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> studentService.saveStudent(expected));

        verify(groupRepository, times(1)).findById(1L);
        verify(studentRepository, never()).save(expected);
    }

    @Test
    void shouldReturnTrueIfStudentWithIdExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student(1L, "John", "Jackson",
                "Jackson", 1, null)));
        assertTrue(studentService.isStudentWithIdExist(1L));
    }

    @Test
    void shouldReturnFalseIfStudentWithIdNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.isStudentWithIdExist(1L));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfStudentNotExistOnDelete() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> studentService.deleteStudentById(1L));

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).deleteById(1L);
    }
}
