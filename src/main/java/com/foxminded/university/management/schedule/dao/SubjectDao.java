package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.SubjectRowMapper;
import com.foxminded.university.management.schedule.models.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SubjectDao extends AbstractDao<Subject> implements Dao<Subject, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDao.class);
    private final JdbcTemplate jdbcTemplate;

    public SubjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Subject create(Subject subject) {
        LOGGER.debug("Creating subject: {}", subject);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("subjects").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", subject.getName());
        params.put("university_id", subject.getUniversityId());

        Number newId;
        try {
            newId = simpleJdbcInsert.executeAndReturnKey(params);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create subject with id: " + subject.getId() +
                    ". Subject with name: " + subject.getName() + " is already exist");
        }
        LOGGER.info("Subject created successful with id: {}", newId);
        return new Subject(newId.longValue(), subject.getName(), subject.getUniversityId());
    }

    @Override
    protected Subject update(Subject subject) {
        LOGGER.debug("Updating subject: {}", subject);
        try {
            this.jdbcTemplate.update("UPDATE subjects SET name = ?, university_id = ? WHERE id = ?",
                    subject.getName(), subject.getUniversityId(), subject.getId());
            LOGGER.info("Subject updated successful: {}", subject);
            return new Subject(subject.getId(), subject.getName(), subject.getUniversityId());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to update subject with id: " + subject.getId() +
                    ". Subject with name: " + subject.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Subject> getById(Long id) {
        LOGGER.debug("Getting subject by id: {}", id);
        Optional<Subject> subject = this.jdbcTemplate.query("SELECT * FROM subjects WHERE id = ?", new SubjectRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Received subject by id: {}. Received subject: {}", id, subject);
        return subject;
    }

    @Override
    public List<Subject> getAll() {
        LOGGER.debug("Getting all subjects");
        List<Subject> subjects = this.jdbcTemplate.query("SELECT * FROM subjects", new SubjectRowMapper());
        LOGGER.info("Subjects received successful");
        return subjects;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting subject with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM subjects WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted subject with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Subject> saveAll(List<Subject> subjects) {
        LOGGER.debug("Saving subjects: {}", subjects);
        List<Subject> result = new ArrayList<>();
        for (Subject subject : subjects) {
            result.add(save(subject));
        }
        LOGGER.info("Successful saved all subjects");
        return result;
    }

    public List<Subject> getSubjectsByTeacherId(Long id) {
        LOGGER.debug("Getting subjects with teacher id: {}", id);
        List<Subject> subjects = this.jdbcTemplate.query("SELECT * FROM subjects " +
                "JOIN subjects_teachers ON subjects_teachers.subject_id = subjects.id " +
                "WHERE subjects_teachers.teacher_id = ?", new SubjectRowMapper(), id);
        LOGGER.info("Successful received subjects with teacher id: {}", id);
        return subjects;
    }

    public List<Subject> getSubjectsByStudentId(Long id) {
        LOGGER.debug("Getting subjects with student id: {}", id);
        List<Subject> subjects = this.jdbcTemplate.query("SELECT * FROM subjects " +
                "JOIN subjects_students ON subjects_students.subject_id = subjects.id " +
                "WHERE subjects_students.student_id = ?", new SubjectRowMapper(), id);
        LOGGER.info("Successful received student with teacher id: {}", id);
        return subjects;
    }

    public List<Subject> getSubjectsByUniversityId(Long id) {
        LOGGER.debug("Getting subjects with university id: {}", id);
        List<Subject> subjects = this.jdbcTemplate.query("SELECT * FROM subjects WHERE university_id = ?", new SubjectRowMapper(), id);
        LOGGER.info("Successful received subjects with university id: {}", id);
        return subjects;
    }
}
