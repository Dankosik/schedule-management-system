package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.repository.FacultyRepository;
import com.foxminded.university.management.schedule.repository.TeacherRepository;
import com.foxminded.university.management.schedule.service.TeacherService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
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
    private final TeacherRepository teacherRepository;
    private final FacultyRepository facultyRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, FacultyRepository facultyRepository) {
        this.teacherRepository = teacherRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        boolean isFacultyPresent = facultyRepository.findById(teacher.getFaculty().getId()).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        LOGGER.debug("Teacher faculty id: {}", teacher.getFaculty().getId());
        if (isFacultyPresent || teacher.getFaculty().getId() == null) {
            return teacherRepository.save(teacher);
        }
        throw new EntityNotFoundException("Teacher's faculty with id: " + teacher.getFaculty().getId() + " is not exists");
    }

    @Override
    public Teacher getTeacherById(Long id) {
        boolean isTeacherPresent = teacherRepository.findById(id).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (isTeacherPresent) {
            return teacherRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Teacher with id: " + id + " is not found");
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public void deleteTeacherById(Long id) {
        boolean isTeacherPresent = teacherRepository.findById(id).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (isTeacherPresent) {
            teacherRepository.deleteById(getTeacherById(id).getId());
        } else {
            throw new EntityNotFoundException("Teacher with id: " + id + " is not found");
        }
    }

    @Override
    public List<Teacher> saveAllTeachers(List<Teacher> teachers) {
        List<Teacher> result = new ArrayList<>();
        teachers.forEach(teacher -> result.add(saveTeacher(teacher)));
        return result;
    }

    @Override
    public List<String> getLastNamesWithInitialsWithPossibleNullForTeachers(List<Teacher> teachers) {
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
    public List<Teacher> getTeachersWithPossibleNullForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting teachers for lectures {}", lectures);
        List<Teacher> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getTeacher() == null) {
                result.add(null);
            } else {
                result.add(lecture.getTeacher());
            }
        }
        LOGGER.info("Teachers for lectures {} received successful", lectures);
        return result;
    }

    public boolean isTeacherWithIdExist(Long id) {
        return teacherRepository.findById(id).isPresent();
    }
}
