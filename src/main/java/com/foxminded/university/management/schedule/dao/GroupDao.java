package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.GroupRowMapper;
import com.foxminded.university.management.schedule.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class GroupDao extends AbstractDao<Group> implements Dao<Group> {
    private final JdbcTemplate jdbcTemplate;

    public GroupDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public GroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Group create(Group group) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("groups").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", group.getName());
        params.put("lecture_id", group.getLectureId());
        params.put("department_id", group.getDepartmentId());
        params.put("faculty_id", group.getFacultyId());
        params.put("university_id", group.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Group(newId.longValue(), group.getName(), group.getLectureId(), group.getDepartmentId(),
                group.getFacultyId(), group.getUniversityId());
    }

    @Override
    protected Group update(Group group) {
        this.jdbcTemplate.update("UPDATE groups SET name = ?, lecture_id = ?, department_id = ?, faculty_id = ?, university_id = ? WHERE id = ?",
                group.getName(), group.getLectureId(), group.getDepartmentId(), group.getFacultyId(), group.getUniversityId(), group.getId());
        return new Group(group.getId(), group.getName(), group.getLectureId(), group.getDepartmentId(),
                group.getFacultyId(), group.getUniversityId());
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
    public boolean delete(Group group) {
        return this.jdbcTemplate.update("DELETE FROM groups WHERE id = ?", group.getId()) == 1;
    }

    @Override
    public List<Group> saveAll(List<Group> groups) {
        List<Group> result = new ArrayList<>();
        for (Group group : groups) {
            result.add(save(group));
        }
        return result;
    }

    public List<Group> getGroupsByLectureId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE lecture_id = ?", new GroupRowMapper(), id);
    }

    public List<Group> getGroupsByDepartmentId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE department_id = ?", new GroupRowMapper(), id);
    }

    public List<Group> getGroupsByFacultyId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE faculty_id = ?", new GroupRowMapper(), id);
    }

    public List<Group> getGroupsByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM groups WHERE university_id = ?", new GroupRowMapper(), id);
    }
}
