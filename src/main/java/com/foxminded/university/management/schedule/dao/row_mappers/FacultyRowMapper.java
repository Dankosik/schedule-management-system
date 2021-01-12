package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Faculty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyRowMapper implements RowMapper<Faculty> {
    @Override
    public Faculty mapRow(ResultSet resultSet, int i) throws SQLException {
        Faculty faculty = new Faculty();
        faculty.setId(resultSet.getLong("id"));
        faculty.setName(resultSet.getString("name"));
        faculty.setUniversityId(resultSet.getLong("university_id"));
        return faculty;
    }
}