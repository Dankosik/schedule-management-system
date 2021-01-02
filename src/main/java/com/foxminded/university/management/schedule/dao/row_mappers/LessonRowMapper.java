package com.foxminded.university.management.schedule.dao.row_mappers;

import com.foxminded.university.management.schedule.models.Lesson;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;


public class LessonRowMapper implements RowMapper<Lesson> {
    @Override
    public Lesson mapRow(ResultSet resultSet, int i) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setId(resultSet.getLong("id"));
        lesson.setNumber(resultSet.getInt("number"));
        lesson.setStartTime(resultSet.getTime("start_time"));
        lesson.setDuration(Duration.ofMinutes(resultSet.getLong("duration")));
        lesson.setSubjectId(resultSet.getLong("subject_id"));
        return lesson;
    }
}
