package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.SubjectRowMapper;
import com.foxminded.university.management.schedule.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class SubjectDao extends AbstractDao<Subject> implements Dao<Subject, Long> {
    private final JdbcTemplate jdbcTemplate;

    public SubjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Subject create(Subject subject) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("subjects").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", subject.getName());
        params.put("university_id", subject.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Subject(newId.longValue(), subject.getName(), subject.getUniversityId());
    }

    @Override
    protected Subject update(Subject subject) {
        this.jdbcTemplate.update("UPDATE subjects SET name = ?, university_id = ? WHERE id = ?",
                subject.getName(), subject.getUniversityId(), subject.getId());
        return new Subject(subject.getId(), subject.getName(), subject.getUniversityId());
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
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM subjects WHERE id = ?", id) == 1;
    }

    @Override
    public List<Subject> saveAll(List<Subject> subjects) {
        List<Subject> result = new ArrayList<>();
        for (Subject subject : subjects) {
            result.add(save(subject));
        }
        return result;
    }

    public List<Subject> getSubjectsByTeacherId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM subjects " +
                "JOIN subjects_teachers ON subjects_teachers.subject_id = subjects.id " +
                "WHERE subjects_teachers.teacher_id = ?", new SubjectRowMapper(), id);
    }

    public List<Subject> getSubjectsByStudentId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM subjects " +
                "JOIN subjects_students ON subjects_students.subject_id = subjects.id " +
                "WHERE subjects_students.student_id = ?", new SubjectRowMapper(), id);
    }

    public List<Subject> getSubjectsByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM subjects WHERE university_id = ?", new SubjectRowMapper(), id);
    }
}
