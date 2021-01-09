package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Teacher;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherRowMapper implements RowMapper<Teacher> {
    @Override
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getLong("id"));
        teacher.setFirstName(resultSet.getString("first_name"));
        teacher.setLastName(resultSet.getString("last_name"));
        teacher.setMiddleName(resultSet.getString("middle_name"));
        teacher.setUniversityId(resultSet.getLong("university_id"));
        return teacher;
    }
}
