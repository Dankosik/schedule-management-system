package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.TeacherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    private final TeacherDao teacherDao;
    private final FacultyDao facultyDao;

    public TeacherServiceImpl(TeacherDao teacherDao, FacultyDao facultyDao) {
        this.teacherDao = teacherDao;
        this.facultyDao = facultyDao;
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        boolean isFacultyPresent = facultyDao.getById(teacher.getFacultyId()).isPresent();
        if (isFacultyPresent || teacher.getFacultyId() == null) {
            return teacherDao.save(teacher);
        }
        throw new ServiceException("Teacher's faculty with id: " + teacher.getFacultyId() + " is not exists");
    }

    @Override
    public Teacher getTeacherById(Long id) {
        if (teacherDao.getById(id).isPresent()) {
            return teacherDao.getById(id).get();
        }
        throw new ServiceException("Teacher with id: " + id + " is not found");
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
