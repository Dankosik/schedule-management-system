package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.TeacherRowMapper;
import com.foxminded.university.management.schedule.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TeacherDao extends AbstractDao<Teacher> implements Dao<Teacher, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDao.class);
    private final JdbcTemplate jdbcTemplate;

    public TeacherDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Teacher create(Teacher teacher) {
        LOGGER.debug("Creating teacher: {}", teacher);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("teachers").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("first_name", teacher.getFirstName());
        params.put("last_name", teacher.getLastName());
        params.put("middle_name", teacher.getMiddleName());
        params.put("faculty_id", teacher.getFacultyId());
        params.put("university_id", teacher.getUniversityId());

        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        LOGGER.info("Teacher created successful with id: {}", newId);
        return new Teacher(newId.longValue(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFacultyId(), teacher.getUniversityId());
    }

    @Override
    protected Teacher update(Teacher teacher) {
        LOGGER.debug("Updating teacher: {}", teacher);
        this.jdbcTemplate.update("UPDATE teachers SET first_name = ?, last_name = ?, middle_name = ?, faculty_id = ?, university_id = ? WHERE id = ?",
                teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(), teacher.getFacultyId(), teacher.getUniversityId(), teacher.getId());
        LOGGER.info("Teacher updated successful: {}", teacher);
        return new Teacher(teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFacultyId(), teacher.getUniversityId());
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        LOGGER.debug("Getting teacher by id: {}", id);
        Optional<Teacher> teacher = this.jdbcTemplate.query("SELECT * FROM teachers WHERE id = ?", new TeacherRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Successful received teacher by id: {}. Received teacher: {}", id, teacher);
        return teacher;
    }

    @Override
    public List<Teacher> getAll() {
        LOGGER.debug("Getting all teachers");
        List<Teacher> teachers = this.jdbcTemplate.query("SELECT * FROM teachers", new TeacherRowMapper());
        LOGGER.info("Teachers received successful");
        return teachers;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting teacher with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM teachers WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted teacher with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Teacher> saveAll(List<Teacher> teachers) {
        LOGGER.debug("Saving teachers: {}", teachers);
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            result.add(save(teacher));
        }
        LOGGER.info("Successful saved all teachers");
        return result;
    }

    public List<Teacher> getTeachersByUniversityId(Long id) {
        LOGGER.debug("Getting teachers with university id: {}", id);
        List<Teacher> teachers = this.jdbcTemplate.query("SELECT * FROM teachers WHERE university_id = ?", new TeacherRowMapper(), id);
        LOGGER.info("Successful received teachers with university id: {}", id);
        return teachers;
    }

    public List<Teacher> getTeachersByFacultyId(Long id) {
        LOGGER.debug("Getting teachers with faculty id: {}", id);
        List<Teacher> teachers = this.jdbcTemplate.query("SELECT * FROM teachers WHERE faculty_id = ?", new TeacherRowMapper(), id);
        LOGGER.info("Successful received teachers with faculty id: {}", id);
        return teachers;
    }
}
