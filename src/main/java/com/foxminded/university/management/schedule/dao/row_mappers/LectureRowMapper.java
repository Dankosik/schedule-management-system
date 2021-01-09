package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Lecture;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LectureRowMapper implements RowMapper<Lecture> {
    @Override
    public Lecture mapRow(ResultSet resultSet, int i) throws SQLException {
        Lecture lecture = new Lecture();
        lecture.setId(resultSet.getLong("id"));
        lecture.setNumber(resultSet.getInt("number"));
        lecture.setDate(resultSet.getDate("date"));
        lecture.setAudienceId(resultSet.getLong("audience_id"));
        lecture.setLessonId(resultSet.getLong("lesson_id"));
        lecture.setTeacherId(resultSet.getLong("teacher_id"));
        return lecture;
    }
}
