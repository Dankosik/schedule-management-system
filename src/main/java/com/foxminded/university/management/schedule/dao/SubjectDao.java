package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.SubjectRowMapper;
import com.foxminded.university.management.schedule.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

@Component
public class SubjectDao extends AbstractDao<Subject> implements Dao<Subject> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SubjectDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected Subject create(Subject subject) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("subjects").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", subject.getName());
        params.put("student_id", subject.getStudentId());
        params.put("teacher_id", subject.getTeacherId());
        params.put("university_id", subject.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Subject((long) newId.intValue(), subject.getName(), subject.getStudentId(), subject.getTeacherId(), subject.getUniversityId());
    }

    @Override
    protected Subject update(Subject subject) {
        this.jdbcTemplate.update("UPDATE subjects SET name = ?, student_id = ?, teacher_id = ?,  university_id = ? WHERE id = ?",
                subject.getName(), subject.getStudentId(), subject.getTeacherId(), subject.getUniversityId(), subject.getId());
        return new Subject(subject.getId(), subject.getName(), subject.getStudentId(), subject.getTeacherId(), subject.getUniversityId());
    }

    @Override
    public Optional<Subject> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM subjects WHERE id = ?", new SubjectRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Subject> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM subjects", new SubjectRowMapper());
    }

    @Override
    public boolean delete(Subject subject) {
        return this.jdbcTemplate.update("DELETE FROM audiences WHERE id = ?", subject.getId()) == 1;
    }

    @Override
    public List<Subject> saveAll(List<Subject> subjects) {
        List<Subject> result = new ArrayList<>();
        for (Subject subject : subjects) {
            result.add(save(subject));
        }
        return result;
    }
}
