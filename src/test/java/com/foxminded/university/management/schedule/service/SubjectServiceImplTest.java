package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SubjectServiceImplTest {
    private final Subject subject = new Subject(1L, "Math", 1L);
    private final List<Subject> subjects = List.of(subject,
            new Subject(2L, "Art", 1L),
            new Subject(3L, "Programming", 1L));

    @Autowired
    private SubjectServiceImpl subjectService;
    @MockBean
    private SubjectDao subjectDao;

    @Test
    void shouldSaveSubject() {
        when(subjectDao.save(new Subject("Math", 1L))).thenReturn(subject);

        Subject actual = subjectService.saveSubject(subject);

        assertEquals(subject, actual);

        verify(subjectDao, times(1)).save(subject);
    }

    @Test
    void shouldReturnSubjectWithIdOne() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));

        Subject actual = subjectService.getSubjectById(1L);

        assertEquals(subject, actual);

        verify(subjectDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfSubjects() {
        when(subjectDao.getAll()).thenReturn(subjects);

        assertEquals(subjects, subjectService.getAllSubjects());

        verify(subjectDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteStudentWithIdOne() {
        when(subjectDao.getById(1L)).thenReturn(Optional.of(subject));
        when(subjectDao.deleteById(1L)).thenReturn(true);

        subjectService.deleteSubjectById(1L);

        verify(subjectDao, times(1)).deleteById(1L);
        verify(subjectDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfAudiences() {
        when(subjectDao.save(new Subject("Math", 1L)))
                .thenReturn(subject);
        when(subjectDao.save(new Subject("Art", 1L)))
                .thenReturn(subjects.get(1));
        when(subjectDao.save(new Subject("Programming", 1L)))
                .thenReturn(subjects.get(2));

        List<Subject> actual = subjectService.saveAllSubjects(subjects);

        assertEquals(subjects, actual);

        verify(subjectDao, times(1)).save(subjects.get(0));
        verify(subjectDao, times(1)).save(subjects.get(1));
        verify(subjectDao, times(1)).save(subjects.get(2));
    }

    @Test
    void shouldThrowExceptionIfSubjectWithInputNameIsAlreadyExist() {
        Subject expected = new Subject("Math", 1L);
        when(subjectDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> subjectService.saveSubject(expected));

        verify(subjectDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfSubjectWithInputIdNotFound() {
        when(subjectDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> subjectService.getSubjectById(1L));

        verify(subjectDao, times(1)).getById(1L);
        verify(subjectDao, never()).save(subject);
    }
}
