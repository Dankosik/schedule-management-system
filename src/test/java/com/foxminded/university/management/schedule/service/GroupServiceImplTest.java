package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
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
@SpringBootTest
class GroupServiceImplTest {
    private final Group group = new Group(1L, "AB-01", 1L);
    private final List<Group> groups = List.of(group,
            new Group(2L, "BC-02", 1L),
            new Group(3L, "CD-03", 1L));
    private final Student student = new Student(1L, "John", "Jackson", "Jackson", 1, null);
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
        when(groupDao.save(new Group("AB-01", 1L))).thenReturn(group);
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT")));

        Group actual = groupService.saveGroup(group);

        assertEquals(group, actual);

        verify(groupDao, times(1)).save(group);
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

        assertEquals(groups, groupService.getAllGroups());

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
        when(groupDao.save(new Group("AB-01", 1L))).thenReturn(group);
        when(groupDao.save(new Group("BC-02", 1L))).thenReturn(groups.get(1));
        when(groupDao.save(new Group("CD-03", 1L))).thenReturn(groups.get(2));
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT")));

        List<Group> actual = groupService.saveAllGroups(groups);

        assertEquals(groups, actual);

        verify(groupDao, times(1)).save(groups.get(0));
        verify(groupDao, times(1)).save(groups.get(1));
        verify(groupDao, times(1)).save(groups.get(2));
    }

    @Test
    void shouldAddStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 1L);
        when(studentService.saveStudent(new Student("John", "Jackson", "Jackson", 1, 1L)))
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
        when(studentService.saveStudent(new Student("John", "Jackson", "Jackson", 1, null)))
                .thenReturn(student);

        Student actual = groupService.removeStudentFromGroup(
                new Student(1L, "John", "Jackson", "Jackson", 1, 1L), group);

        assertEquals(student, actual);

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, times(1)).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfUpdatedGroupWithInputNameIsAlreadyExist() {
        Group expected = new Group("AB-01", 1L);

        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT")));
        when(groupDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(groupDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfCreatedGroupWithInputNameIsAlreadyExist() {
        Group expected = new Group("AB-01", 1L);

        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty("FAIT")));
        when(groupDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(groupDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfGroupWithInputIdNotFound() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.getGroupById(1L));

        verify(groupDao, times(1)).getById(1L);
        verify(groupDao, never()).save(group);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInAddingStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));

        assertThrows(ServiceException.class, () -> groupService.addStudentToGroup(student, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentNotPresentInAddingStudentToGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.addStudentToGroup(student, group));

        verify(groupDao, never()).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfGroupNotPresentInRemovingStudentFromGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());
        when(studentDao.getById(1L)).thenReturn(Optional.of(student));

        assertThrows(ServiceException.class, () -> groupService.removeStudentFromGroup(student, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentNotPresentInRemovingStudentFromGroup() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.removeStudentFromGroup(student, group));

        verify(groupDao, never()).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(student);
    }

    @Test
    void shouldThrowExceptionIfStudentIsAlreadyAddedToGroup() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> groupService.addStudentToGroup(expected, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(expected);
    }

    @Test
    void shouldThrowExceptionIfStudentIsAlreadyRemovedFromGroup() {
        Student expected = new Student(1L, "John", "Jackson", "Jackson", 1, null);

        when(groupDao.getById(1L)).thenReturn(Optional.of(group));
        when(studentDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> groupService.removeStudentFromGroup(expected, group));

        verify(groupDao, times(1)).getById(1L);
        verify(studentDao, times(1)).getById(1L);
        verify(studentService, never()).saveStudent(expected);
    }

    @Test
    void shouldThrowExceptionIfGroupFacultyNotFound() {
        Group expected = new Group(1L, "AB-01", 1L);

        when(groupDao.getById(1L)).thenReturn(Optional.of(expected));
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, never()).save(expected);
    }

    @Test
    void shouldReturnGroupNamesForStudents() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));
        when(groupDao.getById(2L)).thenReturn(Optional.of(new Group(2L, "CD-21", 1L)));

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L));

        List<String> expected = List.of("AB-01", "CD-21");

        assertEquals(expected, groupService.getGroupNamesForStudents(students));

        verify(groupDao, times(2)).getById(1L);
        verify(groupDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnGroupsForStudents() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));
        when(groupDao.getById(2L)).thenReturn(Optional.of(new Group(2L, "CD-21", 1L)));

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 2L));

        List<Group> expected = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(2L, "CD-21", 1L));

        assertEquals(expected, groupService.getGroupsForStudents(students));

        verify(groupDao, times(2)).getById(1L);
        verify(groupDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnGroupNamesForStudentsWithIdZero() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 0L));

        List<String> expected = Arrays.asList("AB-01", null);

        assertEquals(expected, groupService.getGroupNamesForStudents(students));

        verify(groupDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnGroupsForStudentsWithIdZero() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));

        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1, 1L),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, 0L));

        List<Group> expected = Arrays.asList(
                new Group(1L, "AB-01", 1L), null);

        assertEquals(expected, groupService.getGroupsForStudents(students));

        verify(groupDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnGroupsForFaculty() {
        List<Group> expected = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(2L, "CD-21", 1L));

        when(groupDao.getGroupsByFacultyId(1L)).thenReturn(expected);

        assertEquals(expected, groupService.getGroupsForFaculty(new Faculty(1L, "AB-01")));

        verify(groupDao, times(1)).getGroupsByFacultyId(1L);
    }

    @Test
    void shouldReturnGroupNamesForLectures() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));
        when(groupDao.getById(2L)).thenReturn(Optional.of(new Group(2L, "CD-21", 1L)));

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 1L));

        List<String> expected = List.of("AB-01", "CD-21");

        assertEquals(expected, groupService.getGroupNamesForLectures(lectures));

        verify(groupDao, times(2)).getById(1L);
        verify(groupDao, times(2)).getById(2L);
    }

    @Test
    void shouldReturnGroupsForLectures() {
        when(groupDao.getById(1L)).thenReturn(Optional.of(new Group(1L, "AB-01", 1L)));
        when(groupDao.getById(2L)).thenReturn(Optional.of(new Group(2L, "CD-21", 1L)));

        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 2L, 2L, 1L));

        List<Group> expected = List.of(
                new Group(1L, "AB-01", 1L),
                new Group(2L, "CD-21", 1L));

        assertEquals(expected, groupService.getGroupsForLectures(lectures));

        verify(groupDao, times(2)).getById(1L);
        verify(groupDao, times(2)).getById(2L);
    }
}
