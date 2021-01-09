package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.TeacherRowMapper;
import com.foxminded.university.management.schedule.models.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TeacherDao extends AbstractDao<Teacher> implements Dao<Teacher, Long> {
    private final JdbcTemplate jdbcTemplate;

    public TeacherDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Teacher create(Teacher teacher) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("teachers").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", teacher.getFirstName());
        params.put("last_name", teacher.getLastName());
        params.put("middle_name", teacher.getMiddleName());
        params.put("faculty_id", teacher.getFacultyId());
        params.put("university_id", teacher.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Teacher(newId.longValue(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFacultyId(), teacher.getUniversityId());
    }

    @Override
    protected Teacher update(Teacher teacher) {
        this.jdbcTemplate.update("UPDATE teachers SET first_name = ?, last_name = ?, middle_name = ?, faculty_id = ?, university_id = ? WHERE id = ?",
                teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),  teacher.getFacultyId(), teacher.getUniversityId(), teacher.getId());
        return new Teacher(teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFacultyId(),  teacher.getUniversityId());
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM teachers WHERE id = ?", new TeacherRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Teacher> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM teachers", new TeacherRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM teachers WHERE id = ?", id) == 1;
    }

    @Override
    public List<Teacher> saveAll(List<Teacher> teachers) {
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            result.add(save(teacher));
        }
        return result;
    }

    public List<Teacher> getTeachersByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM teachers WHERE university_id = ?", new TeacherRowMapper(), id);
    }

    public List<Teacher> getTeachersByFacultyId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM teachers WHERE faculty_id = ?", new TeacherRowMapper(), id);
    }
}
