package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao;
    private final GroupDao groupDao;

    public StudentServiceImpl(StudentDao studentDao, GroupDao groupDao) {
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }

    @Override
    public Student saveStudent(Student student) {
        boolean isGroupPresent = groupDao.getById(student.getGroupId()).isPresent();
        if (isGroupPresent || student.getGroupId() == null) {
            return studentDao.save(student);
        }
        throw new ServiceException("Student's group with id: " + student.getGroupId() + "is not exist");
    }

    @Override
    public Student getStudentById(Long id) {
        if (studentDao.getById(id).isPresent()) {
            return studentDao.getById(id).get();
        }
        throw new ServiceException("Student with id: " + id + " is not found");
    }

    @Override
    public List<Student> getAllStudent() {
        return studentDao.getAll();
    }

    @Override
    public void deleteStudentById(Long id) {
        studentDao.deleteById(getStudentById(id).getId());
    }

    @Override
    public List<Student> saveAllStudents(List<Student> students) {
        List<Student> result = new ArrayList<>();
        students.forEach(student -> result.add(saveStudent(student)));
        return result;
    }
}
