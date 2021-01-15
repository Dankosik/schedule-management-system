package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.TeacherServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private FacultyDao facultyDao;

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        boolean isFacultyPresent = facultyDao.getById(teacher.getFacultyId()).isPresent();
        if (isFacultyPresent || teacher.getFacultyId() == null) {
            return teacherDao.save(teacher);
        }
        throw new TeacherServiceException("Teacher faculty with id: " + teacher.getFacultyId() + " not exist");
    }

    @Override
    public Teacher getTeacherById(Long id) {
        if (teacherDao.getById(id).isPresent()) {
            return teacherDao.getById(id).get();
        }
        throw new TeacherServiceException("Teacher with id: " + id + " not found");
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherDao.getAll();
    }

    @Override
    public void deleteTeacherById(Long id) {
        teacherDao.deleteById(getTeacherById(id).getId());
    }

    @Override
    public List<Teacher> saveAllTeachers(List<Teacher> teachers) {
        List<Teacher> result = new ArrayList<>();
        teachers.forEach(teacher -> result.add(saveTeacher(teacher)));
        return result;
    }
}
