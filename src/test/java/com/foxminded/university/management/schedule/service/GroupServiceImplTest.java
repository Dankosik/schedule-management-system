package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.exceptions.GroupServiceException;
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
class GroupServiceImplTest {
    private final Group group = new Group(1L, "AB-01", 1L, 1L);
    private final List<Group> groups = List.of(group,
            new Group(2L, "BC-02", 1L, 1L),
            new Group(3L, "CD-03", 1L, 1L));
    private final Student student = new Student(1L, "John", "Jackson", "Jackson", 1, null, 1L);
    @Autowired
    private GroupServiceImpl groupService;

    @MockBean
    private GroupDao groupDao;
    @MockBean
    private StudentDao studentDao;
    @MockBean
    private FacultyDao facultyDao;
    @MockBean
    private StudentServiceImpl studentService;

    @Test
    void shouldSaveGroup() {
        when(groupDao.save(new Group("AB-01", 1L, 1L))).thenReturn(group);
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", 1L)));

        Group actual = groupService.saveGroup(group);

        assertEquals(group, actual);

        verify(groupDao, times(1)).save(group);
        verify(groupDao, times(1)).getAll();
    }

    @Test
    void shouldReturnGroupWithIdOne() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));

        Group actual = groupService.getGroupById(1L);

        assertEquals(group, actual);

        verify(groupDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfGroups() {
        when(groupDao.getAll()).thenReturn(groups);

        assertEquals(groups, groupService.getAllAGroups());

        verify(groupDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteGroupWithIdOne() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(groupDao.deleteById(1L)).thenReturn(true);

        groupService.deleteGroupById(1L);

        verify(groupDao, times(1)).deleteById(1L);
        verify(groupDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfGroups() {
        when(groupDao.save(new Group("AB-01", 1L, 1L))).thenReturn(group);
        when(groupDao.save(new Group("BC-02", 1L, 1L))).thenReturn(groups.get(1));
        when(groupDao.save(new Group("CD-03", 1L, 1L))).thenReturn(groups.get(2));
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", 1L)));

        List<Group> actual = groupService.saveAllGroups(groups);

        assertEquals(groups, actual);

        verify(groupDao, times(1)).save(groups.get(0));
        verify(groupDao, times(1)).save(groups.get(1));
        verify(groupDao, times(1)).save(groups.get(2));
        verify(groupDao, times(3)).getAll();
    }

    @Test
    void shouldAddStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 1L, 1L);
        when(studentService.saveStudent(new Student("John", "Jackson", "Jackson", 1, 1L, 1L)))
                .thenReturn(expected);

        Student actual = groupService.addStudentToGroup(student, group);

        assertEquals(expected, actual);

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, times(1)).saveStudent(expected);
    }

    @Test
    void shouldRemoveStudentFromGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));
        when(studentService.saveStudent(new Student("John", "Jackson", "Jackson", 1, null, 1L)))
                .thenReturn(student);

        Student actual = groupService.removeStudentFromGroup(
                new Student(1L, "John", "Jackson", "Jackson", 1, 1L, 1L), group);

        assertEquals(student, actual);

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfGroupWithInputNameIsAlreadyExist() {
        when(groupDao.getAll()).thenReturn(List.of(group));

        assertThrows(GroupServiceException.class, () -> groupService.saveGroup(group));

        verify(groupDao, times(1)).getAll();
        verify(groupDao, never()).save(group);
    }

    @Test
    void shouldThrowExceptionIfGroupWithInputIdNotFound() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(GroupServiceException.class, () -> groupService.getGroupById(1L));

        verify(groupDao, times(1)).getById(1L);
        verify(groupDao, never()).save(group);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInAddingStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));

        assertThrows(GroupServiceException.class, () -> groupService.addStudentToGroup(student, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentNotPresentInAddingStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(GroupServiceException.class, () -> groupService.addStudentToGroup(student, group));

        verify(groupDao, never()).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInRemovingStudentFromGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));

        assertThrows(GroupServiceException.class, () -> groupService.removeStudentFromGroup(student, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentNotPresentInRemovingStudentFromGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(GroupServiceException.class, () -> groupService.removeStudentFromGroup(student, group));

        verify(groupDao, never()).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentIsAlreadyAddedToGroup() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 1L, 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(GroupServiceException.class, () -> groupService.addStudentToGroup(expected, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(expected);
    }

    @Test
    void shouldThrowExceptionIfStudentIsAlreadyRemovedFromGroup() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, null, 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(GroupServiceException.class, () -> groupService.removeStudentFromGroup(expected, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(expected);
    }

    @Test
    void shouldThrowExceptionIfGroupFacultyNotFound() {
        Group expected = new Group(1L, "AB-01", 1L, 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(expected));
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(GroupServiceException.class, () -> groupService.saveGroup(expected));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, never()).save(expected);
    }

}
