package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.group.GroupDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GroupDtoUtils {
    private static FacultyServiceImpl facultyService;

    public GroupDtoUtils(FacultyServiceImpl facultyService) {
        GroupDtoUtils.facultyService = facultyService;
    }

    public static boolean isSuchFacultyFromGroupDtoExist(GroupDto groupDto) {
        Faculty faculty = facultyService.getFacultyById(groupDto.getFaculty().getId());
        String facultyDtoDtoName = groupDto.getFaculty().getName();
        return faculty.getName().equals(facultyDtoDtoName);
    }

    public static Group mapGroupDtoOnGroup(GroupDto groupDto) {
        Group group = new Group();
        copyFacultyFromGroupDtoToGroup(groupDto, group);
        BeanUtils.copyProperties(groupDto, group);
        return group;
    }

    private static void copyFacultyFromGroupDtoToGroup(GroupDto groupDto, Group group) {
        Faculty faculty = new Faculty();
        BeanUtils.copyProperties(groupDto.getFaculty(), faculty);
        group.setFaculty(faculty);
    }
}
