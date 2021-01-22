package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.LessonRowMapper;
import com.foxminded.university.management.schedule.models.Lesson;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.*;

@Repository
public class LessonDao extends AbstractDao<Lesson> implements Dao<Lesson, Long> {
    private final Logger LOGGER = LoggerFactory.getLogger(LessonDao.class);
    private final JdbcTemplate jdbcTemplate;

    public LessonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Lesson create(Lesson lesson) {
        LOGGER.debug("Creating lesson: {}", lesson);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("lessons").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("number", lesson.getNumber());
        params.put("start_time", lesson.getStartTime());
        params.put("duration", lesson.getDuration());
        params.put("subject_id", lesson.getSubjectId());

        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        LOGGER.info("Lesson created successful with id: {}", newId);
        return new Lesson(newId.longValue(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(), lesson.getSubjectId());
    }

    @Override
    protected Lesson update(Lesson lesson) {
        LOGGER.debug("Updating lesson: {}", lesson);
        this.jdbcTemplate.update("UPDATE lessons SET number = ?, start_time = ?,  duration = ?, subject_id = ? WHERE id = ?",
                lesson.getNumber(), lesson.getStartTime(), convertDurationToHourAndMinutePgInterval(lesson), lesson.getSubjectId(), lesson.getId());
        LOGGER.info("Lesson updated successful: {}", lesson);
        return new Lesson(lesson.getId(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(), lesson.getSubjectId());
    }


    @Override
    public Optional<Lesson> getById(Long id) {
        LOGGER.debug("Getting lesson by id: {}", id);
        Optional<Lesson> lesson = this.jdbcTemplate.query("SELECT * FROM lessons WHERE id = ?", new LessonRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Successful received lesson by id: {}. Received lesson: {}", id, lesson);
        return lesson;
    }

    @Override
    public List<Lesson> getAll() {
        LOGGER.debug("Getting all lessons");
        List<Lesson> lessons = this.jdbcTemplate.query("SELECT * FROM lessons", new LessonRowMapper());
        LOGGER.info("Lessons received successful");
        return lessons;

    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting lesson with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM lessons WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted lesson with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Lesson> saveAll(List<Lesson> lessons) {
        LOGGER.debug("Saving lessons: {}", lessons);
        List<Lesson> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            result.add(save(lesson));
        }
        LOGGER.info("Successful saved all lessons");
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
        LOGGER.debug("Getting lessons with subject id: {}", id);
        List<Lesson> lessons = this.jdbcTemplate.query("SELECT * FROM lessons WHERE subject_id = ?", new LessonRowMapper(), id);
        LOGGER.info("Successful received lessons with subject id: {}", id);
        return lessons;
    }
}
