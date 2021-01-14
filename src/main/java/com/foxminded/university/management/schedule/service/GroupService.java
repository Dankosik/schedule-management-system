package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group saveGroup(Group group);

    Optional<Group> getGroupById(Long id);

    List<Group> getAllAGroups();

    boolean deleteGroupById(Long id);

    List<Group> saveAllGroups(List<Group> groups);

    boolean addStudentToGroup(Student student, Group group);

    boolean removeStudentFromGroup(Student student, Group group);
}
