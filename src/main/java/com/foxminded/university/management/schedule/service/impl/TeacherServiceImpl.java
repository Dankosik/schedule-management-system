package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.TeacherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherServiceImpl.class);
    private final TeacherDao teacherDao;
    private final FacultyDao facultyDao;

    public TeacherServiceImpl(TeacherDao teacherDao, FacultyDao facultyDao) {
        this.teacherDao = teacherDao;
        this.facultyDao = facultyDao;
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        boolean isFacultyPresent = facultyDao.getById(teacher.getFacultyId()).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        LOGGER.debug("Teacher faculty id: {}", teacher.getFacultyId());
        if (isFacultyPresent || teacher.getFacultyId() == null) {
            return teacherDao.save(teacher);
        }
        throw new ServiceException("Teacher's faculty with id: " + teacher.getFacultyId() + " is not exists");
    }

    @Override
    public Teacher getTeacherById(Long id) {
        boolean isTeacherPresent = teacherDao.getById(id).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (isTeacherPresent) {
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

    @Override
    public List<String> getLastNamesWithInitialsForTeachers(List<Teacher> teachers) {
        LOGGER.debug("Getting last names with initials for teachers {}", teachers);
        List<String> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher != null) {
                char firstName = teacher.getFirstName().charAt(0);
                char middleName = teacher.getMiddleName().charAt(0);
                String lastName = teacher.getLastName();
                result.add(lastName + " " + firstName + ". " + middleName + ".");
            } else {
                result.add(null);
            }
        }
        LOGGER.info("Last names with initials for teachers {} received successful", teachers);
        return result;
    }

    @Override
    public List<Teacher> getTeachersForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting teachers for lectures {}", lectures);
        List<Teacher> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getTeacherId() == 0) {
                result.add(null);
            } else {
                result.add(getTeacherById(lecture.getTeacherId()));
            }
        }
        LOGGER.info("Teachers for lectures {} received successful", lectures);
        return result;
    }

    @Override
    public List<Teacher> getTeachersForFaculty(Faculty faculty) {
        LOGGER.debug("Getting teachers for faculty {}", faculty);
        List<Teacher> teachers = teacherDao.getTeachersByFacultyId(faculty.getId());
        LOGGER.info("Teachers for faculty {} received successful", faculty);
        return teachers;
    }
}
