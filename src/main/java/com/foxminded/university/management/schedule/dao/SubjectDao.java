package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SubjectDao extends AbstractDao<Subject> implements Dao<Subject, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDao.class);
    private final EntityManager entityManager;

    public SubjectDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Subject create(Subject subject) {
        LOGGER.debug("Creating subject: {}", subject);

        try {
            entityManager.persist(subject);
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to create subject with id: " + subject.getId() +
                    ". Subject with name: " + subject.getName() + " is already exist");
        }
        LOGGER.info("Subject created successful with id: {}", subject.getId());
        return new Subject(subject.getId(), subject.getName(), subject.getLessons());
    }

    @Override
    protected Subject update(Subject subject) {
        LOGGER.debug("Updating subject: {}", subject);
        try {
            entityManager.merge(subject);
            entityManager.flush();
            LOGGER.info("Subject updated successful: {}", subject);
            return new Subject(subject.getId(), subject.getName(), subject.getLessons());
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to update subject with id: " + subject.getId() +
                    ". Subject with name: " + subject.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Subject> getById(Long id) {
        LOGGER.debug("Getting subject by id: {}", id);
        Subject subject = entityManager.find(Subject.class, id);
        LOGGER.info("Received subject by id: {}. Received subject: {}", id, subject);
        return subject != null ? Optional.of(subject) : Optional.empty();
    }

    @Override
    public List<Subject> getAll() {
        LOGGER.debug("Getting all subjects");
        List<Subject> subjects = entityManager.createQuery("select subject from Subject subject", Subject.class).getResultList();
        LOGGER.info("Subjects received successful");
        return subjects;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting subject with id: {}", id);
        Subject subject = entityManager.find(Subject.class, id);
        if (subject == null) {
            return false;
        }
        entityManager.remove(subject);
        LOGGER.info("Successful deleted subject with id: {}", id);
        return true;
    }

    @Override
    public List<Subject> saveAll(List<Subject> subjects) {
        LOGGER.debug("Saving subjects: {}", subjects);
        List<Subject> result = new ArrayList<>();
        for (Subject subject : subjects) {
            result.add(save(subject));
        }
        LOGGER.info("Successful saved all subjects");
        return result;
    }
}
