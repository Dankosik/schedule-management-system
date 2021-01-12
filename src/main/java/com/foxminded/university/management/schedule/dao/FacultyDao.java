package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.FacultyRowMapper;
import com.foxminded.university.management.schedule.models.Faculty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FacultyDao extends AbstractDao<Faculty> implements Dao<Faculty, Long> {
    private final JdbcTemplate jdbcTemplate;

    public FacultyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Faculty create(Faculty faculty) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("faculties").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", faculty.getName());
        params.put("university_id", faculty.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Faculty(newId.longValue(), faculty.getName(), faculty.getUniversityId());
    }

    @Override
    protected Faculty update(Faculty faculty) {
        this.jdbcTemplate.update("UPDATE faculties SET name = ?, university_id = ? WHERE id = ?",
                faculty.getName(), faculty.getUniversityId(), faculty.getId());
        return new Faculty(faculty.getId(), faculty.getName(), faculty.getUniversityId());
    }

    @Override
    public Optional<Faculty> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM faculties WHERE id = ?", new FacultyRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Faculty> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM faculties", new FacultyRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM faculties WHERE id = ?", id) == 1;
    }

    @Override
    public List<Faculty> saveAll(List<Faculty> faculties) {
        List<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties) {
            result.add(save(faculty));
        }
        return result;
    }

    public List<Faculty> getFacultiesByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM faculties WHERE university_id = ?", new FacultyRowMapper(), id);
    }
}