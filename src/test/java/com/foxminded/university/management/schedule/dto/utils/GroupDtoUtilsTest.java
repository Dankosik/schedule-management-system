package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupAddDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GroupDtoUtils.class})
class GroupDtoUtilsTest {
    @MockBean
    private FacultyServiceImpl facultyService;

    @Test
    void shouldReturnTrueIfSuchFacultyFromGroupAddDtoExist() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "FAIT", null, null));
        assertTrue(GroupDtoUtils.isSuchFacultyFromGroupDtoExist(new GroupAddDto("AB-01", new FacultyUpdateDto(1L, "FAIT"))));
    }

    @Test
    void shouldReturnFalseIfSuchFacultyFromGroupAddDtoNotExist() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "QEWQ", null, null));
        assertFalse(GroupDtoUtils.isSuchFacultyFromGroupDtoExist(new GroupAddDto("AB-01", new FacultyUpdateDto(1L, "FAIT"))));
    }

    @Test
    void shouldReturnTrueIfSuchFacultyFromGroupUpdateDtoExist() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "FAIT", null, null));
        assertTrue(GroupDtoUtils.isSuchFacultyFromGroupDtoExist(new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT"))));
    }

    @Test
    void shouldReturnFalseIfSuchFacultyFromGroupUpdateDtoNotExist() {
        when(facultyService.getFacultyById(1L)).thenReturn(new Faculty(1L, "QEWQ", null, null));
        assertFalse(GroupDtoUtils.isSuchFacultyFromGroupDtoExist(new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT"))));
    }

    @Test
    void shouldReturnGroupFromGroupUpdateDto() {
        Group expected = new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null);
        GroupUpdateDto groupUpdateDto = new GroupUpdateDto(1L, "AB-01", new FacultyUpdateDto(1L, "FAIT"));
        assertEquals(expected, GroupDtoUtils.mapGroupDtoOnGroup(groupUpdateDto));
    }

    @Test
    void shouldReturnGroupFromGroupAddDto() {
        Group expected = new Group(1L, "AB-01", new Faculty(1L, "FAIT", null, null), null, null);
        GroupAddDto groupAddDto = new GroupAddDto("AB-01", new FacultyUpdateDto(1L, "FAIT"));
        assertEquals(expected, GroupDtoUtils.mapGroupDtoOnGroup(groupAddDto));
    }
}
