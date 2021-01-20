package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
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
        Optional<Group> groupWithSameName = groupDao.getAll()
                .stream()
                .filter(g -> g.getName().equals(group.getName()))
                .findAny();

        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (groupWithSameName.isPresent() && !isGroupPresent)
            throw new ServiceException("Group with name: " + group.getName() + " is already exist");

        boolean isFacultyPresent = facultyDao.getById(group.getFacultyId()).isPresent();
        if (!isFacultyPresent)
            throw new ServiceException("Group's faculty with id: " + group.getFacultyId() + " is not exist");

        return groupDao.save(group);
    }

    @Override
    public Group getGroupById(Long id) {
        if (groupDao.getById(id).isPresent()) {
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
        boolean isStudentPresent = studentDao.getById(student.getId()).isPresent();
        if (!isStudentPresent)
            throw new ServiceException("Impossible to add student to group. Student with id: " + student.getId() + " is not exist");

        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (!isGroupPresent)
            throw new ServiceException("Impossible to add student to group. Group with id: " + group.getId() + " is not exist");

        if (student.getGroupId() != null && student.getGroupId().equals(group.getId()))
            throw new ServiceException("Student with id: " + student.getId() + " is already added to group with id: " + group.getId());

        student.setGroupId(group.getId());
        return studentService.saveStudent(student);
    }

    @Override
    public Student removeStudentFromGroup(Student student, Group group) {
        boolean isStudentPresent = studentDao.getById(student.getId()).isPresent();
        if (!isStudentPresent)
            throw new ServiceException("Impossible to remove student from group. Student with id: " + student.getId() + " is not exist");
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (!isGroupPresent)
            throw new ServiceException("Impossible to remove student from group. Group with id: " + group.getId() + " is not exist");

        if (student.getGroupId() == null)
            throw new ServiceException("Student with id: " + student.getId() + "is already removed from group with id: " + group.getId());

        student.setGroupId(null);
        return studentService.saveStudent(student);
    }
}
