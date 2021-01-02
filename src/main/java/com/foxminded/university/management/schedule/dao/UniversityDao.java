package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.StudentRowMapper;
import com.foxminded.university.management.schedule.dao.row_mappers.UniversityRowMapper;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.models.University;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class UniversityDao extends AbstractDao<University> implements Dao<University>{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UniversityDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected University create(University university) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("university").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("schedule_id", university.getScheduleId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new University((long) newId.intValue(), university.getScheduleId());
    }

    @Override
    protected University update(University university) {
        this.jdbcTemplate.update("UPDATE university SET schedule_id = ? WHERE id = ?", university.getScheduleId(), university.getId());
        return new University(university.getId(), university.getScheduleId());
    }

    @Override
    public Optional<University> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM university WHERE id = ?", new UniversityRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<University> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM university", new UniversityRowMapper());
    }

    @Override
    public boolean delete(University university) {
        return this.jdbcTemplate.update("DELETE FROM university WHERE id = ?", university.getId()) == 1;
    }

    @Override
    public List<University> saveAll(List<University> universities) {
        List<University> result = new ArrayList<>();
        for (University university : universities) {
            result.add(save(university));
        }
        return result;
    }
}
