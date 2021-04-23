package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.repository.FacultyRepository;
import com.foxminded.university.management.schedule.repository.GroupRepository;
import com.foxminded.university.management.schedule.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final GroupRepository groupRepository;
    private final FacultyRepository facultyRepository;

    public GroupServiceImpl(GroupRepository groupRepository, FacultyRepository facultyRepository) {
        this.groupRepository = groupRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Group saveGroup(Group group) {
        boolean isFacultyPresent = facultyRepository.findById(group.getFaculty().getId()).isPresent();
        LOGGER.debug("Audience is present: {}", isFacultyPresent);
        if (!isFacultyPresent)
            throw new ServiceException("Group's faculty with id: " + group.getFaculty().getId() + " is not exist");
        try {
            return groupRepository.saveAndFlush(group);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Group getGroupById(Long id) {
        boolean isGroupPresent = groupRepository.findById(id).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (isGroupPresent) {
            return groupRepository.findById(id).get();
        }
        throw new ServiceException("Group with id: " + id + " is not found");
    }

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public void deleteGroupById(Long id) {
        groupRepository.deleteById(getGroupById(id).getId());
    }

    @Override
    public List<Group> saveAllGroups(List<Group> groups) {
        List<Group> result = new ArrayList<>();
        groups.forEach(group -> result.add(saveGroup(group)));
        return result;
    }

    @Override
    public List<String> getGroupNamesWithPossibleNullForStudents(List<Student> students) {
        LOGGER.debug("Getting group names for students {}", students);
        List<String> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getGroup() == null) {
                result.add(null);
            } else {
                result.add(student.getGroup().getName());
            }
        }
        LOGGER.info("Group names for students {} received successful", students);
        return result;
    }

    @Override
    public List<Group> getGroupsWithPossibleNullForStudents(List<Student> students) {
        LOGGER.debug("Getting groups for students {}", students);
        List<Group> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getGroup() == null) {
                result.add(null);
            } else {
                result.add(student.getGroup());
            }
        }
        LOGGER.info("Groups for students {} received successful", students);
        return result;
    }

    @Override
    public List<String> getGroupNamesForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting group names for lectures {}", lectures);
        List<String> groupNames = lectures.stream()
                .map(lecture -> lecture.getGroup().getName())
                .collect(Collectors.toList());
        LOGGER.info("Group names for lectures {} received successful", lectures);
        return groupNames;
    }

    @Override
    public List<Group> getGroupsForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting groups for lectures {}", lectures);
        List<Group> groups = lectures
                .stream()
                .map(Lecture::getGroup)
                .collect(Collectors.toList());
        LOGGER.info("Groups for lectures {} received successful", lectures);
        return groups;
    }

    public boolean isGroupWithIdExist(Long id) {
        return groupRepository.findById(id).isPresent();
    }
}
