package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.University;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UniversityRowMapper implements RowMapper<University> {
    @Override
    public University mapRow(ResultSet resultSet, int i) throws SQLException {
        University university = new University();
        university.setId(resultSet.getLong("id"));
        university.setScheduleId(resultSet.getLong("schedule_id"));
        return university;
    }
}
