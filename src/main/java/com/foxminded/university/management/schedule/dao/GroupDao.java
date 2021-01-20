package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.GroupRowMapper;
import com.foxminded.university.management.schedule.models.Group;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GroupDao extends AbstractDao<Group> implements Dao<Group, Long> {
    private final JdbcTemplate jdbcTemplate;

    public GroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Group create(Group group) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("groups").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", group.getName());
        params.put("faculty_id", group.getFacultyId());
        params.put("university_id", group.getUniversityId());
        Number newId;
        try {
            newId = simpleJdbcInsert.executeAndReturnKey(params);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
        return new Group(newId.longValue(), group.getName(), group.getFacultyId(), group.getUniversityId());
    }

    @Override
    protected Group update(Group group) {
        try{
            this.jdbcTemplate.update("UPDATE groups SET name = ?,  faculty_id = ?, university_id = ? WHERE id = ?",
                    group.getName(), group.getFacultyId(), group.getUniversityId(), group.getId());
            return new Group(group.getId(), group.getName(), group.getFacultyId(), group.getUniversityId());
        } catch (DuplicateKeyException e){
            throw new DuplicateKeyException("Impossible to update group with id: " + group.getId() +
                    ". Group with name: " + group.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Group> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE id = ?", new GroupRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Group> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM groups", new GroupRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM groups WHERE id = ?", id) == 1;
    }

    @Override
    public List<Group> saveAll(List<Group> groups) {
        List<Group> result = new ArrayList<>();
        for (Group group : groups) {
            result.add(save(group));
        }
        return result;
    }

    public List<Group> getGroupsByFacultyId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE faculty_id = ?", new GroupRowMapper(), id);
    }

    public List<Group> getGroupsByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE university_id = ?", new GroupRowMapper(), id);
    }
}
