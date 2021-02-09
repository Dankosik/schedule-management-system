package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final FacultyDao facultyDao;
    private final StudentServiceImpl studentService;

    public GroupServiceImpl(GroupDao groupDao, StudentDao studentDao, FacultyDao facultyDao, StudentServiceImpl studentService) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.facultyDao = facultyDao;
        this.studentService = studentService;
    }

    @Override
    public Group saveGroup(Group group) {
        boolean isFacultyPresent = facultyDao.getById(group.getFacultyId()).isPresent();
        LOGGER.debug("Audience is present: {}", isFacultyPresent);
        if (!isFacultyPresent)
            throw new ServiceException("Group's faculty with id: " + group.getFacultyId() + " is not exist");
        try {
            return groupDao.save(group);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Group getGroupById(Long id) {
        boolean isGroupPresent = groupDao.getById(id).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (isGroupPresent) {
            return groupDao.getById(id).get();
        }
        throw new ServiceException("Group with id: " + id + " is not found");
    }

    @Override
    public List<Group> getAllAGroups() {
        return groupDao.getAll();
    }

    @Override
    public void deleteGroupById(Long id) {
        groupDao.deleteById(getGroupById(id).getId());
    }

    @Override
    public List<Group> saveAllGroups(List<Group> groups) {
        List<Group> result = new ArrayList<>();
        groups.forEach(group -> result.add(saveGroup(group)));
        return result;
    }

    @Override
    public Student addStudentToGroup(Student student, Group group) {
        LOGGER.debug("Adding student {} to group {}", student, group);
        boolean isStudentPresent = studentDao.getById(student.getId()).isPresent();
        LOGGER.debug("Student is present: {}", isStudentPresent);
        if (!isStudentPresent)
            throw new ServiceException("Impossible to add student to group. Student with id: " + student.getId() + " is not exist");

        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (!isGroupPresent)
            throw new ServiceException("Impossible to add student to group. Group with id: " + group.getId() + " is not exist");

        boolean isStudentAlreadyAddedToGroup = student.getGroupId() != null && student.getGroupId().equals(group.getId());
        LOGGER.debug("Student is already added to group: {}", isStudentAlreadyAddedToGroup);
        if (isStudentAlreadyAddedToGroup)
            throw new ServiceException("Student with id: " + student.getId() + " is already added to group with id: " + group.getId());

        student.setGroupId(group.getId());
        Student result = studentService.saveStudent(student);
        LOGGER.info("Successful adding lecture to audience");
        return result;
    }

    @Override
    public Student removeStudentFromGroup(Student student, Group group) {
        LOGGER.debug("Removing student {} from group {}", student, group);
        boolean isStudentPresent = studentDao.getById(student.getId()).isPresent();
        LOGGER.debug("Student is present: {}", isStudentPresent);
        if (!isStudentPresent)
            throw new ServiceException("Impossible to remove student from group. Student with id: " + student.getId() + " is not exist");
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (!isGroupPresent)
            throw new ServiceException("Impossible to remove student from group. Group with id: " + group.getId() + " is not exist");

        boolean isStudentAlreadyRemovedFromGroup = student.getGroupId() == null;
        LOGGER.debug("Student is already removed from group: {}", isStudentAlreadyRemovedFromGroup);
        if (isStudentAlreadyRemovedFromGroup)
            throw new ServiceException("Student with id: " + student.getId() + "is already removed from group with id: " + group.getId());

        student.setGroupId(null);
        Student result = studentService.saveStudent(student);
        LOGGER.info("Successful removing student {} from group {}", student, group);
        return result;
    }

    @Override
    public List<String> getGroupNamesForStudents(List<Student> students) {
        LOGGER.debug("Getting group names for students {}", students);
        List<String> result = new ArrayList<>();
        students.forEach(student -> result.add(getGroupById(student.getGroupId()).getName()));
        LOGGER.info("Group names for students {} received successful", students);
        return result;
    }

    @Override
    public List<Group> getGroupsForStudents(List<Student> students) {
        LOGGER.debug("Getting groups for students {}", students);
        List<Group> groups = students.stream()
                .map(student -> getGroupById(student.getGroupId()))
                .collect(Collectors.toList());
        LOGGER.info("Group for students {} received successful", students);
        return groups;
    }

    @Override
    public List<Group> getGroupsForFaculty(Faculty faculty) {
        LOGGER.debug("Getting group for faculty {}", faculty);
        List<Group> groups = groupDao.getGroupsByFacultyId(faculty.getId());
        LOGGER.info("Groups for faculty {} received successful", faculty);
        return groups;
    }
}
