package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.repository.FacultyRepository;
import com.foxminded.university.management.schedule.service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty saveFaculty(Faculty faculty) {
        try {
            return facultyRepository.saveAndFlush(faculty);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("Faculty with name: " + faculty.getName() + " is already exist");
        }
    }

    @Override
    public Faculty getFacultyById(Long id) {
        boolean isFacultyPresent = facultyRepository.findById(id).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        if (isFacultyPresent) {
            return facultyRepository.findById(id).get();
        }
        throw new ServiceException("Faculty with id: " + id + " is not found");
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteFacultyById(Long id) {
        facultyRepository.deleteById(getFacultyById(id).getId());
    }

    @Override
    public List<Faculty> saveAllFaculties(List<Faculty> faculties) {
        List<Faculty> result = new ArrayList<>();
        faculties.forEach(faculty -> result.add(saveFaculty(faculty)));
        return result;
    }

    @Override
    public List<String> getFacultyNamesForTeachers(List<Teacher> teachers) {
        LOGGER.debug("Getting faculty names for teachers {}", teachers);
        List<String> facultyNames = teachers.stream()
                .map(teacher -> teacher.getFaculty().getName())
                .collect(Collectors.toList());
        LOGGER.info("Faculty names for teachers {} received successful", teachers);
        return facultyNames;
    }

    @Override
    public List<Faculty> getFacultiesForTeachers(List<Teacher> teachers) {
        LOGGER.debug("Getting faculties for teachers {}", teachers);
        List<Faculty> faculties = teachers.stream()
                .map(Teacher::getFaculty)
                .collect(Collectors.toList());
        LOGGER.info("Faculties for teachers {} received successful", teachers);
        return faculties;
    }

    @Override
    public List<Faculty> getFacultiesForGroups(List<Group> groups) {
        LOGGER.debug("Getting faculties for groups {}", groups);
        List<Faculty> faculties = groups.stream()
                .map(Group::getFaculty)
                .collect(Collectors.toList());
        LOGGER.info("Faculties for groups {} received successful", groups);
        return faculties;
    }

    @Override
    public boolean isFacultyWithIdExist(Long id) {
        return facultyRepository.findById(id).isPresent();
    }
}
