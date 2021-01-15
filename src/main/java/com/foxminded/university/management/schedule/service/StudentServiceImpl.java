package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.StudentDao;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.exceptions.StudentServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private GroupDao groupDao;

    @Override
    public Student saveStudent(Student student) {
        boolean isGroupPresent = groupDao.getById(student.getGroupId()).isPresent();
        if (isGroupPresent || student.getGroupId() == null) {
            return studentDao.save(student);
        }
        throw new StudentServiceException("Student group with id: " + student.getGroupId() + " not exist");
    }

    @Override
    public Student getStudentById(Long id) {
        if (studentDao.getById(id).isPresent()) {
            return studentDao.getById(id).get();
        }
        throw new StudentServiceException("Student with id: " + id + " not found");
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
