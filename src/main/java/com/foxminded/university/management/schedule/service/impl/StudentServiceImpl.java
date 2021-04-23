package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.repository.GroupRepository;
import com.foxminded.university.management.schedule.repository.StudentRepository;
import com.foxminded.university.management.schedule.service.StudentService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        boolean isGroupPresent = groupRepository.findById(student.getGroup().getId()).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        LOGGER.debug("Student group id: {}", student.getGroup());
        if (isGroupPresent || student.getGroup() == null) {
            return studentRepository.save(student);
        }
        throw new EntityNotFoundException("Student's group with id: " + student.getGroup() + " is not exist");
    }

    @Override
    public Student getStudentById(Long id) {
        boolean isStudentPresent = studentRepository.findById(id).isPresent();
        LOGGER.debug("Student is present: {}", isStudentPresent);
        if (isStudentPresent) {
            return studentRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Student with id: " + id + " is not found");
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void deleteStudentById(Long id) {
        boolean isStudentPresent = studentRepository.findById(id).isPresent();
        LOGGER.debug("Student is present: {}", isStudentPresent);
        if (isStudentPresent) {
            studentRepository.deleteById(getStudentById(id).getId());
        } else {
            throw new EntityNotFoundException("Student with id: " + id + " is not found");
        }
    }

    @Override
    public List<Student> saveAllStudents(List<Student> students) {
        List<Student> result = new ArrayList<>();
        students.forEach(student -> result.add(saveStudent(student)));
        return result;
    }

    public boolean isStudentWithIdExist(Long id) {
        return studentRepository.findById(id).isPresent();
    }
}
