package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;

import java.util.List;

public interface GroupService {
    Group saveGroup(Group group);

    Group getGroupById(Long id);

    List<Group> getAllAGroups();

    void deleteGroupById(Long id);

    List<Group> saveAllGroups(List<Group> groups);

    Student addStudentToGroup(Student student, Group group);

    Student removeStudentFromGroup(Student student, Group group);

    List<String> getGroupNamesForStudents(List<Student> students);

    List<Group> getGroupsForStudents(List<Student> students);
}
