package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupRowMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getLong("id"));
        group.setName(resultSet.getString("name"));
        group.setLectureId(resultSet.getLong("lecture_id"));
        group.setDepartmentId(resultSet.getLong("department_id"));
        group.setFacultyId(resultSet.getLong("faculty_id"));
        group.setUniversityId(resultSet.getLong("university_id"));
        return group;
    }
}
