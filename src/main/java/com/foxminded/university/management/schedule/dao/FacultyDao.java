package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Faculty;
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
public class FacultyDao extends AbstractDao<Faculty> implements Dao<Faculty, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyDao.class);
    private final EntityManager entityManager;

    public FacultyDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Faculty create(Faculty faculty) {
        LOGGER.debug("Creating faculty: {}", faculty);
        try {
            entityManager.persist(faculty);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to create faculty with id: " + faculty.getId() +
                    ". Faculty with name: " + faculty.getName() + " is already exist");
        }
        LOGGER.info("Faculty created successful with id: {}", faculty.getId());
        return new Faculty(faculty.getId(), faculty.getName(), faculty.getGroups());
    }

    @Override
    protected Faculty update(Faculty faculty) {
        LOGGER.debug("Updating faculty: {}", faculty);
        try {
            entityManager.merge(faculty);
            entityManager.flush();
            LOGGER.info("Faculty updated successful: {}", faculty);
            return new Faculty(faculty.getId(), faculty.getName(), faculty.getGroups());
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to update faculty with id: " + faculty.getId() +
                    ". Faculty with name: " + faculty.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Faculty> getById(Long id) {
        LOGGER.debug("Getting faculty by id: {}", id);
        Faculty faculty = entityManager.find(Faculty.class, id);
        LOGGER.info("Received faculty by id: {}. Received faculty: {}", id, faculty);
        return faculty != null ? Optional.of(faculty) : Optional.empty();
    }

    @Override
    public List<Faculty> getAll() {
        LOGGER.debug("Getting all faculties");
        List<Faculty> faculties = entityManager.createQuery("from Faculty", Faculty.class).getResultList();
        LOGGER.info("Faculties received successful");
        return faculties;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting faculty with id: {}", id);
        Faculty faculty = entityManager.find(Faculty.class, id);
        if (faculty == null) {
            return false;
        }
        entityManager.remove(faculty);
        LOGGER.info("Successful deleted faculty with id: {}", id);
        return true;
    }

    @Override
    public List<Faculty> saveAll(List<Faculty> faculties) {
        LOGGER.debug("Saving faculties: {}", faculties);
        List<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties) {
            result.add(save(faculty));
        }
        LOGGER.info("Successful saved all faculties");
        return result;
    }
}
