package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDao extends AbstractDao<Group> implements Dao<Group, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDao.class);
    private final EntityManager entityManager;

    public GroupDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Group create(Group group) {
        LOGGER.debug("Creating group: {}", group);
        try {
            entityManager.persist(group);
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to create group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
        LOGGER.info("Group created successful with id: {}", group.getId());
        return new Group(group.getId(), group.getName(), group.getFaculty(), group.getStudents(), group.getLectures());
    }

    @Override
    protected Group update(Group group) {
        LOGGER.debug("Updating group: {}", group);
        try {
            entityManager.merge(group);
            entityManager.flush();
            LOGGER.info("Group updated successful: {}", group);
            return new Group(group.getId(), group.getName(), group.getFaculty(), group.getStudents(), group.getLectures());
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to update group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Group> getById(Long id) {
        LOGGER.debug("Getting group by id: {}", id);
        Group group;
        try {
            group = entityManager.createQuery("select g from Group g left join fetch g.lectures le " +
                    "left join fetch le.teacher t left join fetch t.faculty left join fetch le.lesson l left join fetch l.subject " +
                    "left join fetch le.audience  left join fetch g.faculty where g.id =: id", Group.class)
                    .setParameter("id", id)
                    .getSingleResult();

            group = entityManager.createQuery("select g from Group g left join fetch g.faculty left join fetch g.students where g.id =: id", Group.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        LOGGER.info("Received group by id: {}. Received group: {}", id, group);
        return group != null ? Optional.of(group) : Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        LOGGER.debug("Getting all groups");
        List<Group> groups = entityManager.createQuery("select g from Group g join fetch g.faculty", Group.class).getResultList();
        LOGGER.info("Groups received successful");
        return groups;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting group with id: {}", id);
        Group group = entityManager.find(Group.class, id);
        if (group == null) {
            return false;
        }
        entityManager.remove(group);
        LOGGER.info("Successful deleted group with id: {}", id);
        return true;
    }

    @Override
    public List<Group> saveAll(List<Group> groups) {
        LOGGER.debug("Saving groups: {}", groups);
        List<Group> result = new ArrayList<>();
        for (Group group : groups) {
            result.add(save(group));
        }
        LOGGER.info("Successful saved all groups");
        return result;
    }
}
