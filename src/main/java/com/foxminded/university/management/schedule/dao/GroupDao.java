package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDao extends AbstractDao<Group> implements Dao<Group, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDao.class);
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public GroupDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Override
    protected Group create(Group group) {
        LOGGER.debug("Creating group: {}", group);
        try {
            entityManager.persist(group);
            entityManager.flush();
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
        LOGGER.info("Group created successful with id: {}", group.getId());
        return new Group(group.getId(), group.getName(), group.getFaculty());
    }

    @Override
    protected Group update(Group group) {
        LOGGER.debug("Updating group: {}", group);
        try {
            entityManager.merge(group);
            entityManager.flush();
            LOGGER.info("Group updated successful: {}", group);
            return new Group(group.getId(), group.getName(), group.getFaculty());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to update group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Group> getById(Long id) {
        LOGGER.debug("Getting group by id: {}", id);
        Group group = entityManager.find(Group.class, id);
        LOGGER.info("Received group by id: {}. Received group: {}", id, group);
        return group != null ? Optional.of(group) : Optional.empty();
    }

    @Override
    public List<Group> getAll() {
        LOGGER.debug("Getting all groups");
        List<Group> groups = entityManager.createQuery("from Group", Group.class).getResultList();
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

    public List<Group> getGroupsByFacultyId(Long id) {
        LOGGER.debug("Getting groups with faculty id: {}", id);
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("faculty_id"), id));
        List<Group> groups = entityManager.createQuery(criteriaQuery).getResultList();
        LOGGER.info("Successful received groups with faculty id: {}", id);
        return groups;
    }
}
