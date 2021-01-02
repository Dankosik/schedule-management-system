package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.DepartmentRowMapper;
import com.foxminded.university.management.schedule.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class DepartmentDao extends AbstractDao<Department> implements Dao<Department> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DepartmentDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
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
        return new Department((long) newId.intValue(), department.getName(), department.getFacultyId(), department.getUniversityId());
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
    public boolean delete(Department department) {
        return this.jdbcTemplate.update("DELETE FROM departments WHERE id = ?", department.getId()) == 1;
    }

    @Override
    public List<Department> saveAll(List<Department> departments) {
        List<Department> result = new ArrayList<>();
        for (Department department : departments) {
            result.add(save(department));
        }
        return result;
    }
}
