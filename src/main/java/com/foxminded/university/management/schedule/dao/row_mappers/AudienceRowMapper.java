package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Audience;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AudienceRowMapper implements RowMapper<Audience> {
    @Override
    public Audience mapRow(ResultSet resultSet, int i) throws SQLException {
        Audience audience = new Audience();
        audience.setId(resultSet.getLong("id"));
        audience.setNumber(resultSet.getInt("number"));
        audience.setCapacity(resultSet.getInt("capacity"));
        audience.setUniversityId(resultSet.getLong("university_id"));
        return audience;
    }
}
