package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.teacher.TeacherDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TeacherDtoUtils {
    private static FacultyServiceImpl facultyService;

    public TeacherDtoUtils(FacultyServiceImpl facultyService) {
        TeacherDtoUtils.facultyService = facultyService;
    }

    public static boolean isSuchFacultyFromTeacherDtoExist(TeacherDto teacherDto) {
        Faculty faculty = facultyService.getFacultyById(teacherDto.getFaculty().getId());
        String facultyDtoDtoName = teacherDto.getFaculty().getName();
        return faculty.getName().equals(facultyDtoDtoName);
    }

    public static Teacher mapTeacherDtoOnTeacher(TeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        copyFacultyFromTeacherDtoToTeacher(teacherDto, teacher);
        BeanUtils.copyProperties(teacherDto, teacher);
        return teacher;
    }

    private static void copyFacultyFromTeacherDtoToTeacher(TeacherDto teacherDto, Teacher teacher) {
        Faculty faculty = new Faculty();
        BeanUtils.copyProperties(teacherDto.getFaculty(), faculty);
        teacher.setFaculty(faculty);
    }
}
