package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.LessonRowMapper;
import com.foxminded.university.management.schedule.models.Lesson;
import org.postgresql.util.PGInterval;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class LessonDao extends AbstractDao<Lesson> implements Dao<Lesson, Long> {
    private final JdbcTemplate jdbcTemplate;
    public LessonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Lesson create(Lesson lesson) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("lessons").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("number", lesson.getNumber());
        params.put("start_time", lesson.getStartTime());
        params.put("duration", lesson.getDuration());
        params.put("subject_id", lesson.getSubjectId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Lesson(newId.longValue(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(), lesson.getSubjectId());
    }

    @Override
    protected Lesson update(Lesson lesson) {
        this.jdbcTemplate.update("UPDATE lessons SET number = ?, start_time = ?,  duration = ?, subject_id = ? WHERE id = ?",
                lesson.getNumber(), lesson.getStartTime(), convertDurationToHourAndMinutePgInterval(lesson), lesson.getSubjectId(), lesson.getId());
        return new Lesson(lesson.getId(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(), lesson.getSubjectId());
    }


    @Override
    public Optional<Lesson> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lessons WHERE id = ?", new LessonRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Lesson> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM lessons", new LessonRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM lessons WHERE id = ?", id) == 1;
    }

    @Override
    public List<Lesson> saveAll(List<Lesson> lessons) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            result.add(save(lesson));
        }
        return result;
    }

    private PGInterval convertDurationToHourAndMinutePgInterval(Lesson lesson) {
        long minutes = lesson.getDuration().toMinutes();
        PGInterval pgInterval;
        try {
            pgInterval = new PGInterval(minutes / 60 + " hour " + minutes % 60 + " minute");
            return pgInterval;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cant cast duration of lesson to PGInterval");
    }

    public List<Lesson> getLessonsBySubjectId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lessons WHERE subject_id = ?", new LessonRowMapper(), id);
    }
}
