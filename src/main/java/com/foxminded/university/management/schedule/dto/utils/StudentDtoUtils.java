package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.student.StudentDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class StudentDtoUtils {
    private static GroupServiceImpl groupService;

    public StudentDtoUtils(GroupServiceImpl groupService) {
        StudentDtoUtils.groupService = groupService;
    }

    public static Student mapStudentDtoOnStudent(StudentDto studentDto) {
        boolean suchGroupFromStudentDtoExist = isSuchGroupFromStudentDtoExist(studentDto);
        if (!suchGroupFromStudentDtoExist) {
            throw new EntityNotFoundException("Such group does not exist");
        }

        Student student = new Student();
        copyGroupFromStudentDtoToStudent(studentDto, student);

        BeanUtils.copyProperties(studentDto, student);
        return student;
    }

    private static boolean isSuchGroupFromStudentDtoExist(StudentDto studentDto) {
        Group group = groupService.getGroupById(studentDto.getGroup().getId());
        String groupDtoName = studentDto.getGroup().getName();
        FacultyUpdateDto groupDtoFaculty = studentDto.getGroup().getFaculty();
        Faculty mappedGroupDtoFaculty = new Faculty();
        BeanUtils.copyProperties(groupDtoFaculty, mappedGroupDtoFaculty);
        return isSuchGroupExist(group, groupDtoName, mappedGroupDtoFaculty);
    }

    private static void copyGroupFromStudentDtoToStudent(StudentDto studentDto, Student student) {
        Group group = new Group();
        Faculty groupFaculty = new Faculty();
        BeanUtils.copyProperties(studentDto.getGroup().getFaculty(), groupFaculty);
        group.setFaculty(groupFaculty);
        BeanUtils.copyProperties(studentDto.getGroup(), group);
        student.setGroup(group);
    }

    private static boolean isSuchGroupExist(Group group, String groupDtoName, Faculty mappedGroupDtoFaculty) {
        return group.getName().equals(groupDtoName) && group.getFaculty().getName().equals(mappedGroupDtoFaculty.getName()) &&
                group.getFaculty().getId().equals(mappedGroupDtoFaculty.getId());
    }
}
