package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
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
@SpringBootTest(classes = {GroupServiceImpl.class})
class GroupServiceImplTest {
    private final Group group = new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null);
    private final List<Group> groups = List.of(group,
            new Group(2L, "BC-02", new Faculty(1L, "FAIT", null, null), null, null),
            new Group(3L, "CD-03", new Faculty(1L, "FAIT", null, null), null, null));
    private final Student student = new Student(1L, "John", "Jackson", "Jackson", 1, null);
    @Autowired
    private GroupServiceImpl groupService;

    @MockBean
    private GroupDao groupDao;
    @MockBean
    private FacultyDao facultyDao;

    @Test
    void shouldSaveGroup() {
        when(groupDao.save(new Group("AB-01", new Faculty(1L, "FAIT", null, null), null, null))).thenReturn(group);
        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", null, null)));

        Group actual = groupService.saveGroup(new Group("AB-01", new Faculty(1L, "FAIT", null, null), null, null));

        assertEquals(group, actual);

        verify(groupDao, times(1)).save(group);
        verify(facultyDao, times(1)).getById(1L);
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
        Faculty faculty = new Faculty(1L, "FAIT", null, null);
        when(groupDao.save(new Group("AB-01", faculty, null, null)))
                .thenReturn(new Group("AB-01", faculty, null, null));
        when(groupDao.save(new Group("BC-02", faculty, null, null)))
                .thenReturn(new Group("BC-02", faculty, null, null));
        when(groupDao.save(new Group("CD-03", faculty, null, null)))
                .thenReturn(new Group("CD-03", faculty, null, null));
        when(facultyDao.getById(1L)).thenReturn(Optional.of(faculty));

        List<Group> actual = groupService.saveAllGroups(groups);

        assertEquals(groups, actual);

        verify(groupDao, times(1)).save(groups.get(0));
        verify(groupDao, times(1)).save(groups.get(1));
        verify(groupDao, times(1)).save(groups.get(2));
    }

    @Test
    void shouldThrowExceptionIfUpdatedGroupWithInputNameIsAlreadyExist() {
        Group expected = new Group("AB-01", new Faculty(1L, "FAIT", null, null), null, null);

        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty(1L, "FAIT", null, null)));
        when(groupDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(groupDao, times(1)).save(expected);
        verify(facultyDao, times(1)).getById(1L);
    }

    @Test
    void shouldThrowExceptionIfCreatedGroupWithInputNameIsAlreadyExist() {
        Group expected = new Group("AB-01", new Faculty(1L, "FAIT", null, null), null, null);

        when(facultyDao.getById(1L)).thenReturn(Optional.of(new Faculty("FAIT", null, null)));
        when(groupDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(facultyDao, times(1)).getById(1L);
    }

    @Test
    void shouldThrowExceptionIfGroupWithInputIdNotFound() {
        when(groupDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.getGroupById(1L));

        verify(groupDao, times(1)).getById(1L);
        verify(groupDao, never()).save(group);
    }

    @Test
    void shouldThrowExceptionIfGroupFacultyNotFound() {
        Group expected = new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null);

        when(groupDao.getById(1L)).thenReturn(Optional.of(expected));
        when(facultyDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> groupService.saveGroup(expected));

        verify(facultyDao, times(1)).getById(1L);
        verify(groupDao, never()).save(expected);
    }

    @Test
    void shouldReturnGroupNamesForStudents() {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1,
                        new Group(1L, "AB-01", null, null, null)),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1,
                        new Group(2L, "CD-21", null, null, null)));

        List<String> expected = List.of("AB-01", "CD-21");

        assertEquals(expected, groupService.getGroupNamesWithPossibleNullForStudents(students));
    }

    @Test
    void shouldReturnGroupsForStudents() {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1,
                        new Group(1L, "AB-01", null, null, null)),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1,
                        new Group(2L, "CD-21", null, null, null)));

        List<Group> expected = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "CD-21", null, null, null));

        assertEquals(expected, groupService.getGroupsWithPossibleNullForStudents(students));
    }

    @Test
    void shouldReturnGroupNamesForStudentsWithIdZero() {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1,
                        new Group(1L, "AB-01", null, null, null)),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, null));

        List<String> expected = Arrays.asList("AB-01", null);

        assertEquals(expected, groupService.getGroupNamesWithPossibleNullForStudents(students));
    }

    @Test
    void shouldReturnGroupsForStudentsWithIdZero() {
        List<Student> students = List.of(
                new Student(1L, "Ferdinanda", "Casajuana", "Lambarton", 1,
                        new Group(1L, "AB-01", null, null, null)),
                new Student(2L, "Lindsey", "Syplus", "Slocket", 1, null));

        List<Group> expected = Arrays.asList(
                new Group(1L, "AB-01", null, null, null), null);

        assertEquals(expected, groupService.getGroupsWithPossibleNullForStudents(students));
    }

    @Test
    void shouldReturnGroupNamesForLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null,
                        new Group(1L, "AB-01", null, null, null), null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null,
                        new Group(2L, "CD-21", null, null, null), null, null));

        List<String> expected = List.of("AB-01", "CD-21");

        assertEquals(expected, groupService.getGroupNamesForLectures(lectures));
    }

    @Test
    void shouldReturnGroupsForLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), null,
                        new Group(1L, "AB-01", null, null, null), null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null,
                        new Group(2L, "CD-21", null, null, null), null, null));

        List<Group> expected = List.of(
                new Group(1L, "AB-01", null, null, null),
                new Group(2L, "CD-21", null, null, null));

        assertEquals(expected, groupService.getGroupsForLectures(lectures));
    }
}
