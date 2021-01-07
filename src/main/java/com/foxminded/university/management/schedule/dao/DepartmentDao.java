package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.DepartmentRowMapper;
import com.foxminded.university.management.schedule.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class DepartmentDao extends AbstractDao<Department> implements Dao<Department, Long> {
    private final JdbcTemplate jdbcTemplate;

    public DepartmentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public DepartmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Department create(Department department) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("departments").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", department.getName());
        params.put("faculty_id", department.getFacultyId());
        params.put("university_id", department.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Department(newId.longValue(), department.getName(), department.getFacultyId(), department.getUniversityId());
    }

    @Override
    protected Department update(Department department) {
        this.jdbcTemplate.update("UPDATE departments SET name = ?, faculty_id = ?,  university_id = ? WHERE id = ?",
                department.getName(), department.getFacultyId(), department.getUniversityId(), department.getId());
        return new Department(department.getId(), department.getName(), department.getFacultyId(), department.getUniversityId());
    }

    @Override
    public Optional<Department> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM departments WHERE id = ?", new DepartmentRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Department> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM departments", new DepartmentRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM departments WHERE id = ?", id) == 1;
    }

    @Override
    public List<Department> saveAll(List<Department> departments) {
        List<Department> result = new ArrayList<>();
        for (Department department : departments) {
            result.add(save(department));
        }
        return result;
    }

    public List<Department> getDepartmentsByFacultyId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM departments WHERE faculty_id = ?", new DepartmentRowMapper(), id);
    }

    public List<Department> getDepartmentsByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM departments WHERE university_id = ?", new DepartmentRowMapper(), id);
    }
}
