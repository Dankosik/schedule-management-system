package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.StudentRowMapper;
import com.foxminded.university.management.schedule.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class StudentDao extends AbstractDao<Student> implements Dao<Student> {
    private final JdbcTemplate jdbcTemplate;

    public StudentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Student create(Student student) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("students").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", student.getFirstName());
        params.put("last_name", student.getLastName());
        params.put("middle_name", student.getMiddleName());
        params.put("course_number", student.getCourseNumber());
        params.put("group_id", student.getGroupId());
        params.put("faculty_id", student.getFacultyId());
        params.put("university_id", student.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Student(newId.longValue(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroupId(), student.getFacultyId(), student.getUniversityId());
    }

    @Override
    protected Student update(Student student) {
        this.jdbcTemplate.update("UPDATE students SET first_name = ?, last_name = ?, middle_name = ?, " +
                        "course_number = ?, group_id = ?, faculty_id = ?,  university_id = ? WHERE id = ?",
                student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroupId(), student.getFacultyId(), student.getUniversityId(), student.getId());
        return new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroupId(), student.getFacultyId(), student.getUniversityId());
    }

    @Override
    public Optional<Student> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM students WHERE id = ?", new StudentRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Student> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM students", new StudentRowMapper());
    }

    @Override
    public boolean delete(Student student) {
        return this.jdbcTemplate.update("DELETE FROM students WHERE id = ?", student.getId()) == 1;
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            result.add(save(student));
        }
        return result;
    }

    public List<Student> getStudentsByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM students WHERE university_id = ?", new StudentRowMapper(), id);
    }

    public List<Student> getStudentsByGroupId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM students WHERE group_id = ?", new StudentRowMapper(), id);
    }

    public List<Student> getStudentsByFacultyId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM students WHERE faculty_id = ?", new StudentRowMapper(), id);
    }
}
