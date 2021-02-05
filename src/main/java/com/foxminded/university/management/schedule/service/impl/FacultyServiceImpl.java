package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.FacultyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyDao facultyDao;
    private final GroupDao groupDao;
    private final TeacherDao teacherDao;
    private final GroupServiceImpl groupService;
    private final TeacherServiceImpl teacherService;

    public FacultyServiceImpl(FacultyDao facultyDao, GroupDao groupDao, TeacherDao teacherDao, GroupServiceImpl groupService, TeacherServiceImpl teacherService) {
        this.facultyDao = facultyDao;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @Override
    public Faculty saveFaculty(Faculty faculty) {
        try {
            return facultyDao.save(faculty);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("Faculty with name: " + faculty.getName() + " is already exist");
        }
    }

    @Override
    public Faculty getFacultyById(Long id) {
        boolean isFacultyPresent = facultyDao.getById(id).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        if (isFacultyPresent) {
            return facultyDao.getById(id).get();
        }
        throw new ServiceException("Faculty with id: " + id + " is not found");
    }

    @Override
    public List<Faculty> getAllFaculties() {
        return facultyDao.getAll();
    }

    @Override
    public void deleteFacultyById(Long id) {
        facultyDao.deleteById(getFacultyById(id).getId());
    }

    @Override
    public List<Faculty> saveAllFaculties(List<Faculty> faculties) {
        List<Faculty> result = new ArrayList<>();
        faculties.forEach(faculty -> result.add(saveFaculty(faculty)));
        return result;
    }

    @Override
    public Group addGroupToFaculty(Group group, Faculty faculty) {
        LOGGER.debug("Adding group {} to faculty {}", group, faculty);
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (!isGroupPresent)
            throw new ServiceException("Impossible to add group to faculty. Group with id: " + group.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        if (!isFacultyPresent)
            throw new ServiceException("Impossible to add group to faculty. Faculty with id: " + faculty.getId() + " is not exist");

        boolean isGroupAlreadyAddedToFaculty = group.getFacultyId() != null && group.getFacultyId().equals(faculty.getId());
        LOGGER.debug("Group is already added to faculty: {}", isGroupAlreadyAddedToFaculty);
        if (isGroupAlreadyAddedToFaculty)
            throw new ServiceException("Group with id: " + group.getId() + " is already added to faculty with id: " + faculty.getId());

        group.setFacultyId(faculty.getId());
        Group result = groupService.saveGroup(group);
        LOGGER.info("Successful adding group to faculty");
        return result;
    }

    @Override
    public Group removeGroupFromFaculty(Group group, Faculty faculty) {
        LOGGER.debug("Removing group {} from faculty {}", group, faculty);
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        LOGGER.debug("Group is present: {}", isGroupPresent);
        if (!isGroupPresent)
            throw new ServiceException("Impossible to remove group from faculty. Group with id: " + group.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        if (!isFacultyPresent)
            throw new ServiceException("Impossible to remove group from faculty. Faculty with id: " + faculty.getId() + " is not exist");

        boolean isGroupAlreadyRemovedFromFaculty = group.getFacultyId() == null;
        LOGGER.debug("Group is already removed from faculty: {}", isGroupAlreadyRemovedFromFaculty);
        if (isGroupAlreadyRemovedFromFaculty)
            throw new ServiceException("Group with id: " + group.getId() + " is already removed from faculty with id: " + group.getId());

        group.setFacultyId(null);
        Group result = groupService.saveGroup(group);
        LOGGER.info("Successful removing group {} from faculty {}", group, faculty);
        return result;
    }

    @Override
    public Teacher addTeacherToFaculty(Teacher teacher, Faculty faculty) {
        LOGGER.debug("Adding teacher {} to faculty {}", teacher, faculty);
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (!isTeacherPresent)
            throw new ServiceException("Impossible to add teacher to faculty. Teacher with id: " + teacher.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        LOGGER.debug("Faculty is present: {}", isFacultyPresent);
        if (!isFacultyPresent)
            throw new ServiceException("Impossible to add teacher to faculty. Faculty with id: " + faculty.getId() + " is not exist");

        boolean isTeacherAlreadyAddedToFaculty = teacher.getFacultyId() != null && teacher.getFacultyId().equals(faculty.getId());
        LOGGER.debug("Teacher is already added to faculty: {}", isTeacherAlreadyAddedToFaculty);
        if (isTeacherAlreadyAddedToFaculty)
            throw new ServiceException("Teacher with id: " + teacher.getId() + " is already added to faculty with id: " + faculty.getId());

        teacher.setFacultyId(faculty.getId());
        Teacher result = teacherService.saveTeacher(teacher);
        LOGGER.info("Successful adding teacher {} to faculty {}", teacher, faculty);
        return result;
    }

    @Override
    public Teacher removeTeacherFromFaculty(Teacher teacher, Faculty faculty) {
        LOGGER.debug("Removing teacher {} from faculty {}", teacher, faculty);
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new ServiceException("Impossible to remove teacher from faculty cause teacher with id: " + teacher.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (!isFacultyPresent)
            throw new ServiceException("Impossible to remove teacher from faculty cause faculty with id: " + faculty.getId() + " is not exist");

        boolean isTeacherAlreadyRemovedFromFaculty = teacher.getFacultyId() == null;
        LOGGER.debug("Teacher is already removed from faculty: {}", isTeacherAlreadyRemovedFromFaculty);
        if (isTeacherAlreadyRemovedFromFaculty)
            throw new ServiceException("Teacher with id: " + teacher.getId() + " is already removed from faculty with id: " + teacher.getId());

        teacher.setFacultyId(null);
        Teacher result = teacherService.saveTeacher(teacher);
        LOGGER.info("Successful removing teacher {} from faculty {}", teacher, faculty);
        return result;
    }

    @Override
    public List<String> getFacultyNamesForTeachers(List<Teacher> teachers) {
        LOGGER.debug("Getting faculty names for teachers {}", teachers);
        List<String> result = new ArrayList<>();
        teachers.forEach(teacher -> result.add(getFacultyById(teacher.getFacultyId()).getName()));
        LOGGER.info("Faculty names for teachers {} received successful", teachers);
        return result;
    }

    @Override
    public List<Faculty> getFacultiesForTeachers(List<Teacher> teachers) {
        LOGGER.debug("Getting faculties for teachers {}", teachers);
        List<Faculty> faculties = teachers.stream()
                .map(teacher -> getFacultyById(teacher.getFacultyId()))
                .collect(Collectors.toList());
        LOGGER.info("Faculties for teachers {} received successful", teachers);
        return faculties;
    }
}
