package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.exceptions.SubjectServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    @Override
    public Subject saveSubject(Subject subject) {
        Optional<Subject> groupWithSameName = subjectDao.getAll()
                .stream()
                .filter(g -> g.getName().equals(subject.getName()))
                .findAny();
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        if (groupWithSameName.isPresent() && !isSubjectPresent)
            throw new SubjectServiceException("Subject with  name: " + subject.getName() + " already exist");
        return subjectDao.save(subject);
    }

    @Override
    public Subject getSubjectById(Long id) {
        if (subjectDao.getById(id).isPresent()) {
            return subjectDao.getById(id).get();
        }
        throw new SubjectServiceException("Subject with id: " + id + " not found");
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectDao.getAll();
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectDao.deleteById(getSubjectById(id).getId());
    }

    @Override
    public List<Subject> saveAllSubjects(List<Subject> subjects) {
        List<Subject> result = new ArrayList<>();
        subjects.forEach(subject -> result.add(saveSubject(subject)));
        return result;
    }
}
