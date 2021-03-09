package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.StudentRowMapper;
import com.foxminded.university.management.schedule.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class StudentDao extends AbstractDao<Student> implements Dao<Student, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDao.class);
    private final JdbcTemplate jdbcTemplate;

    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Student create(Student student) {
        LOGGER.debug("Creating student: {}", student);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("students").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("first_name", student.getFirstName());
        params.put("last_name", student.getLastName());
        params.put("middle_name", student.getMiddleName());
        params.put("course_number", student.getCourseNumber());
        params.put("group_id", student.getGroupId());

        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        LOGGER.info("Student created successful with id: {}", newId);
        return new Student(newId.longValue(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroupId());
    }

    @Override
    protected Student update(Student student) {
        LOGGER.debug("Updating student: {}", student);
        this.jdbcTemplate.update("UPDATE students SET first_name = ?, last_name = ?, middle_name = ?, " +
                        "course_number = ?, group_id = ? WHERE id = ?",
                student.getFirstName(), student.getLastName(), student.getMiddleName(), student.getCourseNumber(),
                student.getGroupId(), student.getId());
        LOGGER.info("Student updated successful: {}", student);
        return new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroupId());
    }

    @Override
    public Optional<Student> getById(Long id) {
        LOGGER.debug("Getting student by id: {}", id);
        Optional<Student> student = this.jdbcTemplate.query("SELECT * FROM students WHERE id = ?", new StudentRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Received student by id: {}. Received student: {}", id, student);
        return student;
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("Getting all students");
        List<Student> students = this.jdbcTemplate.query("SELECT * FROM students", new StudentRowMapper());
        LOGGER.info("Students received successful");
        return students;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting student with id: {}", id);
        return this.jdbcTemplate.update("DELETE FROM students WHERE id = ?", id) == 1;
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        LOGGER.debug("Saving students: {}", students);
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            result.add(save(student));
        }
        LOGGER.info("Successful saved all students");
        return result;
    }

    public List<Student> getStudentsByGroupId(Long id) {
        LOGGER.debug("Getting students with group id: {}", id);
        List<Student> students = this.jdbcTemplate.query("SELECT * FROM students WHERE group_id = ?", new StudentRowMapper(), id);
        LOGGER.info("Successful received students with group id: {}", id);
        return students;
    }
}
