package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;

import java.util.List;

public interface GroupService {
    Group saveGroup(Group group);

    Group getGroupById(Long id);

    List<Group> getAllGroups();

    void deleteGroupById(Long id);

    List<Group> saveAllGroups(List<Group> groups);

    List<String> getGroupNamesWithPossibleNullForStudents(List<Student> students);

    List<Group> getGroupsWithPossibleNullForStudents(List<Student> students);

    List<String> getGroupNamesForLectures(List<Lecture> lectures);

    List<Group> getGroupsForLectures(List<Lecture> lectures);

    boolean isGroupWithIdExist(Long id);
}
