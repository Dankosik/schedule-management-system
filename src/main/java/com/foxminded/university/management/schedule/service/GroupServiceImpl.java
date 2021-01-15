package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.exceptions.GroupServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentServiceImpl studentService;

    @Override
    public Group saveGroup(Group group) {
        Optional<Group> groupWithSameName = groupDao.getAll()
                .stream()
                .filter(g -> g.getName().equals(group.getName()))
                .findAny();
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (groupWithSameName.isPresent() && !isGroupPresent)
            throw new GroupServiceException("Group with name: " + group.getName() + " already exist");
        return groupDao.save(group);
    }

    @Override
    public Group getGroupById(Long id) {
        if (groupDao.getById(id).isPresent()) {
            return groupDao.getById(id).get();
        }
        throw new GroupServiceException("Group with id: " + id + " not found");
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
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (isStudentPresent && isGroupPresent) {
            student.setGroupId(group.getId());
            return studentService.saveStudent(student);
        }
        if (!isStudentPresent)
            throw new GroupServiceException("Cant add student to group cause student with id: " + student.getId() + " not exist");
        throw new GroupServiceException("Cant add student to group cause group with id: " + group.getId() + " not exist");
    }

    @Override
    public Student removeStudentFromGroup(Student student, Group group) {
        boolean isStudentPresent = studentDao.getById(student.getId()).isPresent();
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (isStudentPresent && isGroupPresent) {
            student.setGroupId(null);
            return studentService.saveStudent(student);
        }
        if (!isStudentPresent)
            throw new GroupServiceException("Cant remove student from group cause student with id: " + student.getId() + " not exist");
        throw new GroupServiceException("Cant remove student from group cause group with id: " + group.getId() + " not exist");
    }
}
