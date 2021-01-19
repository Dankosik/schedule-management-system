package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.FacultyDao;
import com.foxminded.university.management.schedule.dao.GroupDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.AudienceServiceException;
import com.foxminded.university.management.schedule.service.exceptions.FacultyServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {
    @Autowired
    private FacultyDao facultyDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private GroupServiceImpl groupService;
    @Autowired
    private TeacherServiceImpl teacherService;

    @Override
    public Faculty saveFaculty(Faculty faculty) {
        Optional<Faculty> facultyWithSameName = facultyDao.getAll()
                .stream()
                .filter(f -> f.getName().equals(faculty.getName()))
                .findAny();
        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (facultyWithSameName.isPresent() && !isFacultyPresent)
            throw new FacultyServiceException("Faculty with name: " + faculty.getName() + " is already exist");
        return facultyDao.save(faculty);
    }

    @Override
    public Faculty getFacultyById(Long id) {
        if (facultyDao.getById(id).isPresent()) {
            return facultyDao.getById(id).get();
        }
        throw new FacultyServiceException("Faculty with id: " + id + " is not found");
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
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (!isGroupPresent)
            throw new FacultyServiceException("Impossible to add group to faculty. Group with id: " + group.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (!isFacultyPresent)
            throw new FacultyServiceException("Impossible to add group to faculty. Faculty with id: " + faculty.getId() + " is not exist");

        if (group.getFacultyId() != null && group.getFacultyId().equals(faculty.getId()))
            throw new FacultyServiceException("Group with id: " + group.getId() + " is already added to faculty with id: " + faculty.getId());

        group.setFacultyId(faculty.getId());
        return groupService.saveGroup(group);
    }

    @Override
    public Group removeGroupFromFaculty(Group group, Faculty faculty) {
        boolean isGroupPresent = groupDao.getById(group.getId()).isPresent();
        if (!isGroupPresent)
            throw new FacultyServiceException("Impossible to remove group from faculty. Group with id: " + group.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (!isFacultyPresent)
            throw new FacultyServiceException("Impossible to remove group from faculty. Faculty with id: " + faculty.getId() + " is not exist");

        if (group.getFacultyId() == null)
            throw new FacultyServiceException("Group with id: " + group.getId() + " is already removed from faculty with id: " + group.getId());

        group.setFacultyId(null);
        return groupService.saveGroup(group);
    }

    @Override
    public Teacher addTeacherToFaculty(Teacher teacher, Faculty faculty) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new FacultyServiceException("Impossible to add teacher to faculty. Teacher with id: " + teacher.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (!isFacultyPresent)
            throw new FacultyServiceException("Impossible to add teacher to faculty. Faculty with id: " + faculty.getId() + " is not exist");

        if (teacher.getFacultyId() != null && teacher.getFacultyId().equals(faculty.getId()))
            throw new FacultyServiceException("Teacher with id: " + teacher.getId() + " is already added to faculty with id: " + faculty.getId());

        teacher.setFacultyId(faculty.getId());
        return teacherService.saveTeacher(teacher);
    }

    @Override
    public Teacher removeTeacherFromFaculty(Teacher teacher, Faculty faculty) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new FacultyServiceException("Impossible to remove teacher from faculty cause teacher with id: " + teacher.getId() + " is not exist");

        boolean isFacultyPresent = facultyDao.getById(faculty.getId()).isPresent();
        if (!isFacultyPresent)
            throw new FacultyServiceException("Impossible to remove teacher from faculty cause faculty with id: " + faculty.getId() + " is not exist");

        if (teacher.getFacultyId() == null)
            throw new FacultyServiceException("Teacher with id: " + teacher.getId() + " is already removed from faculty with id: " + teacher.getId());

        teacher.setFacultyId(null);
        return teacherService.saveTeacher(teacher);
    }
}
