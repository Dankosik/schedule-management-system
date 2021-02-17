package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.GroupRowMapper;
import com.foxminded.university.management.schedule.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GroupDao extends AbstractDao<Group> implements Dao<Group, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupDao.class);
    private final JdbcTemplate jdbcTemplate;

    public GroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Group create(Group group) {
        LOGGER.debug("Creating group: {}", group);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("groups").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", group.getName());
        params.put("faculty_id", group.getFacultyId());
        params.put("university_id", 1L);

        Number newId;
        try {
            newId = simpleJdbcInsert.executeAndReturnKey(params);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
        LOGGER.info("Group created successful with id: {}", newId);
        return new Group(newId.longValue(), group.getName(), group.getFacultyId(), group.getUniversityId());
    }

    @Override
    protected Group update(Group group) {
        LOGGER.debug("Updating group: {}", group);
        try {
            this.jdbcTemplate.update("UPDATE groups SET name = ?,  faculty_id = ?, university_id = ? WHERE id = ?",
                    group.getName(), group.getFacultyId(), group.getUniversityId(), group.getId());
            LOGGER.info("Group updated successful: {}", group);
            return new Group(group.getId(), group.getName(), group.getFacultyId(), group.getUniversityId());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to update group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Group> getById(Long id) {
        LOGGER.debug("Getting group by id: {}", id);
        Optional<Group> group = this.jdbcTemplate.query("SELECT * FROM groups WHERE id = ?", new GroupRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Received group by id: {}. Received group: {}", id, group);
        return group;
    }

    @Override
    public List<Group> getAll() {
        LOGGER.debug("Getting all groups");
        List<Group> groups = this.jdbcTemplate.query("SELECT * FROM groups", new GroupRowMapper());
        LOGGER.info("Groups received successful");
        return groups;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting group with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM groups WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted group with id: {}", id);
        return isDeleted;
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
        List<Group> groups = this.jdbcTemplate.query("SELECT * FROM groups WHERE faculty_id = ?", new GroupRowMapper(), id);
        LOGGER.info("Successful received groups with faculty id: {}", id);
        return groups;
    }

    public List<Group> getGroupsByUniversityId(Long id) {
        LOGGER.debug("Getting groups with university id: {}", id);
        List<Group> groups = this.jdbcTemplate.query("SELECT * FROM groups WHERE university_id = ?", new GroupRowMapper(), id);
        LOGGER.info("Successful received groups with university id: {}", id);
        return groups;
    }
}
