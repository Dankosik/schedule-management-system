package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Subject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectRowMapper implements RowMapper<Subject> {
    @Override
    public Subject mapRow(ResultSet resultSet, int i) throws SQLException {
        Subject subject = new Subject();
        subject.setId(resultSet.getLong("id"));
        subject.setName(resultSet.getString("name"));
        return subject;
    }
}
