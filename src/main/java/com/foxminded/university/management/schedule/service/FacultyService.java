package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    Faculty saveFaculty(Faculty faculty);

    Optional<Faculty> getFacultyById(Long id);

    List<Faculty> getAllFaculties();

    boolean deleteFacultyById(Long id);

    List<Faculty> saveAllFaculties(List<Faculty> faculties);

    boolean addGroupToFaculty(Group group, Faculty faculty);

    boolean removeGroupFromFaculty(Group group, Faculty faculty);

    boolean addTeacherToFaculty(Teacher teacher, Faculty faculty);

    boolean removeTeacherFromFaculty(Teacher teacher, Faculty faculty);
}
