package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.FacultyServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FacultyServiceImplTest {
    private final Faculty faculty = new Faculty(1L, "FAIT", 1L);
    private final List<Faculty> faculties = List.of(faculty, new Faculty(2L, "FKFN", 1L));
    private final Teacher teacher = new Teacher(1L,"John", "Jackson", "Jackson", null, 1L);
    private final Group group = new Group(1L,"AB-81", null, 1L);
    @Autowired
    private FacultyServiceImpl facultyService;
    @MockBean
    private FacultyDao facultyDao;
    @MockBean
    private GroupDao groupDao;
    @MockBean
    private TeacherDao teacherDao;
    @MockBean
    private GroupServiceImpl groupService;
    @MockBean
    private TeacherServiceImpl teacherService;

    @Test
    void shouldSaveFaculty() {
        when(facultyDao.save(new Faculty("FAIT", 1L))).thenReturn(faculty);
        Faculty actual = facultyService.saveFaculty(faculty);

        assertEquals(faculty, actual);

        verify(facultyDao, times(1)).save(faculty);
        verify(facultyDao, times(1)).getAll();
    }

    @Test
    void shouldReturnFacultyWithIdOne() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        Faculty actual = facultyService.getFacultyById(1L);

        assertEquals(faculty, actual);

        verify(facultyDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfFaculties() {
        when(facultyDao.getAll()).thenReturn(faculties);

        assertEquals(faculties, facultyService.getAllFaculties());

        verify(facultyDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteFacultyWithIdOne() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(facultyDao.deleteById(1L)).thenReturn(true);

        facultyService.deleteFacultyById(1L);

        verify(facultyDao, times(1)).deleteById(1L);
        verify(facultyDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfFaculties() {
        when(facultyDao.save(new Faculty("FAIT", 1L))).thenReturn(faculty);
        when(facultyDao.save(new Faculty("FKFN", 1L))).thenReturn(faculties.get(1));


        List<Faculty> actual = facultyService.saveAllFaculties(faculties);

        assertEquals(faculties, actual);

        verify(facultyDao, times(1)).save(faculties.get(0));
        verify(facultyDao, times(1)).save(faculties.get(1));
        verify(facultyDao, times(2)).getAll();
    }

    @Test
    void shouldAddGroupToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(groupService.saveGroup(new Group("AB-81", 1L, 1L)))
                .thenReturn(new Group(1L, "AB-81", 1L, 1L));

        Group actual = facultyService.addGroupToFaculty(group, faculty);
        assertEquals(new Group(1L, "AB-81", 1L, 1L), actual);

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    void shouldRemoveGroupFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(groupService.saveGroup(new Group("AB-81", null, 1L)))
                .thenReturn(group);

        Group actual = facultyService.removeGroupFromFaculty(group, faculty);
        assertEquals(group, actual);

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, times(1)).saveGroup(group);
    }

    @Test
    void shouldAddTeacherToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(teacherService.saveTeacher(new Teacher("John", "Jackson", "Jackson", 1L, 1L)))
                .thenReturn(new Teacher(1L,"John", "Jackson", "Jackson", 1L, 1L));

        Teacher actual = facultyService.addTeacherToFaculty(teacher, faculty);
        assertEquals(new Teacher(1L,"John", "Jackson", "Jackson", 1L, 1L), actual);

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, times(1)).saveTeacher(teacher);
    }

    @Test
    void shouldRemoveTeacherFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));
        when(teacherService.saveTeacher(new Teacher("John", "Jackson", "Jackson", null, 1L)))
                .thenReturn(teacher);

        Teacher actual = facultyService.removeTeacherFromFaculty(
                new Teacher(1L,"John", "Jackson", "Jackson", 1L, 1L), faculty);
        assertEquals(teacher, actual);

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, times(1)).saveTeacher(teacher);
    }

    @Test
    void shouldThrowExceptionIfFacultyWithInputNameIsAlreadyExist() {
        when(facultyDao.getAll()).thenReturn(List.of(faculty));

        assertThrows(FacultyServiceException.class, ()->facultyService.saveFaculty(faculty));

        verify(facultyDao, times(1)).getAll();
        verify(facultyDao, never()).save(faculty);
    }

    @Test
    void shouldThrowExceptionIfAudienceWithInputIdNotFound() {
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyServiceException.class, ()->facultyService.getFacultyById(1L));

        verify(facultyDao, times(1)).getById(1L);
        verify(facultyDao, never()).save(faculty);
    }

    @Test
    void shouldThrowExceptionIfFacultyNotPresentInAddingGroupToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));

        assertThrows(FacultyServiceException.class, ()->facultyService.addGroupToFaculty(group, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, never()).saveGroup(group);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInAddingGroupToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyServiceException.class, ()->facultyService.addGroupToFaculty(group, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, never()).saveGroup(group);
    }

    @Test
    void shouldThrowExceptionIfFacultyNotPresentInRemovingGroupFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));

        assertThrows(FacultyServiceException.class, ()->facultyService.removeGroupFromFaculty(group, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, never()).saveGroup(group);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInRemovingGroupFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyServiceException.class, ()->facultyService.removeGroupFromFaculty(group, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, times(1)).getById(1L);
        verify(groupService, never()).saveGroup(group);
    }

    @Test
    void shouldThrowExceptionIfFacultyNotPresentInAddingTeacherToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        assertThrows(FacultyServiceException.class, ()->facultyService.addTeacherToFaculty(teacher, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    void shouldThrowExceptionIfTeacherNotPresentInAddingTeacherToFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyServiceException.class, ()->facultyService.addTeacherToFaculty(teacher, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    void shouldThrowExceptionIfFacultyNotPresentInRemovingTeacherFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());
        when(teacherDao.getById(1L)).thenReturn(Optional.of(teacher));

        assertThrows(FacultyServiceException.class, ()->facultyService.removeTeacherFromFaculty(teacher, faculty));
        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, never()).saveTeacher(teacher);
    }

    @Test
    void shouldThrowExceptionIfTeacherNotPresentInRemovingTeacherFromFaculty() {
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));
        when(teacherDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(FacultyServiceException.class, ()->facultyService.removeTeacherFromFaculty(teacher, faculty));

        verify(facultyDao, times(1)).getById(1L);
        verify(teacherDao, times(1)).getById(1L);
        verify(teacherService, never()).saveTeacher(teacher);
    }
}
