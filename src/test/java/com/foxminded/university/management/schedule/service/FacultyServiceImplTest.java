package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.repository.FacultyRepository;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FacultyServiceImpl.class})
class FacultyServiceImplTest {
    private final Faculty faculty = new Faculty(1L, "FAIT", null, null);
    private final List<Faculty> faculties = List.of(faculty, new Faculty(2L, "FKFN", null, null));
    @Autowired
    private FacultyServiceImpl facultyService;
    @MockBean
    private FacultyRepository facultyRepository;

    @Test
    void shouldSaveFaculty() {
        when(facultyRepository.saveAndFlush(new Faculty("FAIT", null, null))).thenReturn(faculty);
        Faculty actual = facultyService.saveFaculty(faculty);

        assertEquals(faculty, actual);

        verify(facultyRepository, times(1)).saveAndFlush(faculty);
    }

    @Test
    void shouldReturnFacultyWithIdOne() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));
        Faculty actual = facultyService.getFacultyById(1L);

        assertEquals(faculty, actual);

        verify(facultyRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfFaculties() {
        when(facultyRepository.findAll()).thenReturn(faculties);

        assertEquals(faculties, facultyService.getAllFaculties());

        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteFacultyWithIdOne() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(faculty));

        facultyService.deleteFacultyById(1L);

        verify(facultyRepository, times(1)).deleteById(1L);
        verify(facultyRepository, times(2)).findById(1L);
    }

    @Test
    void shouldSaveListOfFaculties() {
        when(facultyRepository.saveAndFlush(new Faculty("FAIT", null, null))).thenReturn(faculty);
        when(facultyRepository.saveAndFlush(new Faculty("FKFN", null, null))).thenReturn(faculties.get(1));


        List<Faculty> actual = facultyService.saveAllFaculties(faculties);

        assertEquals(faculties, actual);

        verify(facultyRepository, times(1)).saveAndFlush(faculties.get(0));
        verify(facultyRepository, times(1)).saveAndFlush(faculties.get(1));
    }

    @Test
    void shouldThrowExceptionIfFacultyWithInputIdNotFound() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> facultyService.getFacultyById(1L));

        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyRepository, never()).save(faculty);
    }

    @Test
    void shouldThrowExceptionIfUpdatedAudienceWithInputNumberIsAlreadyExist() {
        Faculty expected = new Faculty(1L, "FAIT", null, null);

        when(facultyRepository.saveAndFlush(expected)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ServiceException.class, () -> facultyService.saveFaculty(expected));

        verify(facultyRepository, times(1)).saveAndFlush(expected);
    }

    @Test
    void shouldThrowExceptionIfAudienceWithInputIdNotFound() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> facultyService.getFacultyById(1L));

        verify(facultyRepository, times(1)).findById(1L);
        verify(facultyRepository, never()).save(faculty);
    }

    @Test
    void shouldReturnFacultyNamesForTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard",
                        new Faculty(1L, "FAIT", null, null), null),
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey",
                        new Faculty(2L, "FKFN", null, null), null));

        List<String> expected = List.of("FAIT", "FKFN");

        assertEquals(expected, facultyService.getFacultyNamesForTeachers(teachers));
    }

    @Test
    void shouldReturnFacultiesForTeachers() {
        List<Teacher> teachers = List.of(
                new Teacher(1L, "Hillel", "St. Leger", "Lugard",
                        new Faculty(1L, "FAIT", null, null), null),
                new Teacher(2L, "Lynsey", "Grzeszczak", "McPhillimey",
                        new Faculty(2L, "FKFN", null, null), null));

        List<Faculty> expected = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null));

        assertEquals(expected, facultyService.getFacultiesForTeachers(teachers));
    }

    @Test
    void shouldReturnFacultiesForGroups() {
        List<Group> groups = List.of(
                new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null),
                new Group(2L, "CD-21", new Faculty(2L, "FKFN", null, null), null, null));

        List<Faculty> expected = List.of(
                new Faculty(1L, "FAIT", null, null),
                new Faculty(2L, "FKFN", null, null));

        assertEquals(expected, facultyService.getFacultiesForGroups(groups));
    }

    @Test
    void shouldReturnTrueIfFacultyWithIdExist() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", null, null)));
        assertTrue(facultyService.isFacultyWithIdExist(1L));
    }

    @Test
    void shouldReturnFalseIfFacultyWithIdNotExist() {
        when(facultyRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(facultyService.isFacultyWithIdExist(1L));
    }
}
