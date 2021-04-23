package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;

public interface FacultyService {
    Faculty saveFaculty(Faculty faculty);

    Faculty getFacultyById(Long id);

    List<Faculty> getAllFaculties();

    void deleteFacultyById(Long id);

    List<Faculty> saveAllFaculties(List<Faculty> faculties);

    List<String> getFacultyNamesForTeachers(List<Teacher> teachers);

    List<Faculty> getFacultiesForTeachers(List<Teacher> teachers);

    List<Faculty> getFacultiesForGroups(List<Group> groups);

    boolean isFacultyWithIdExist(Long id);
}
