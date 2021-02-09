package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.LectureRowMapper;
import com.foxminded.university.management.schedule.models.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LectureDao extends AbstractDao<Lecture> implements Dao<Lecture, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudienceDao.class);
    private final JdbcTemplate jdbcTemplate;

    public LectureDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Lecture create(Lecture lecture) {
        LOGGER.debug("Creating lecture: {}", lecture);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("lectures").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("number", lecture.getNumber());
        params.put("date", lecture.getDate());
        params.put("audience_id", lecture.getAudienceId());
        params.put("group_id", lecture.getGroupId());
        params.put("lesson_id", lecture.getLessonId());
        params.put("teacher_id", lecture.getTeacherId());

        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        LOGGER.info("Lecture created successful with id: {}", newId);
        return new Lecture(newId.longValue(), lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(),
                lecture.getGroupId(), lecture.getLessonId(), lecture.getTeacherId());
    }

    @Override
    protected Lecture update(Lecture lecture) {
        LOGGER.debug("Updating lecture: {}", lecture);
        this.jdbcTemplate.update("UPDATE lectures SET number = ?, date = ?,  audience_id = ?, lesson_id = ?, " +
                        "teacher_id = ? WHERE id = ?",
                lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(), lecture.getLessonId(), lecture.getTeacherId(),
                lecture.getId());
        LOGGER.info("Lecture updated successful: {}", lecture);
        return new Lecture(lecture.getId(), lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(),
                lecture.getGroupId(), lecture.getLessonId(), lecture.getTeacherId());
    }

    @Override
    public Optional<Lecture> getById(Long id) {
        LOGGER.debug("Getting lecture by id: {}", id);
        Optional<Lecture> lecture = this.jdbcTemplate.query("SELECT * FROM lectures WHERE id = ?", new LectureRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Received lecture by id: {}. Received lecture: {}", id, lecture);
        return lecture;
    }

    @Override
    public List<Lecture> getAll() {
        LOGGER.debug("Getting all lectures");
        List<Lecture> lectures = this.jdbcTemplate.query("SELECT * FROM lectures", new LectureRowMapper());
        LOGGER.info("Lectures received successful");
        return lectures;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting lecture with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM lectures WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted lecture with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Lecture> saveAll(List<Lecture> lectures) {
        LOGGER.debug("Saving lectures: {}", lectures);
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            result.add(save(lecture));
        }
        LOGGER.info("Successful saved all lectures");
        return result;
    }

    public List<Lecture> getLecturesByAudienceId(Long id) {
        LOGGER.debug("Getting lectures with audience id: {}", id);
        List<Lecture> lectures = this.jdbcTemplate.query("SELECT * FROM lectures WHERE audience_id = ?", new LectureRowMapper(), id);
        LOGGER.info("Successful received lectures with audience id: {}", id);
        return lectures;
    }

    public List<Lecture> getLecturesByLessonId(Long id) {
        LOGGER.debug("Getting lectures with lesson id: {}", id);
        List<Lecture> lectures = this.jdbcTemplate.query("SELECT * FROM lectures WHERE lesson_id = ?", new LectureRowMapper(), id);
        LOGGER.info("Successful received lectures with lesson id: {}", id);
        return lectures;
    }

    public List<Lecture> getLecturesByTeacherId(Long id) {
        LOGGER.debug("Getting lectures with teacher id: {}", id);
        List<Lecture> lectures = this.jdbcTemplate.query("SELECT * FROM lectures WHERE teacher_id = ?", new LectureRowMapper(), id);
        LOGGER.debug("Getting lectures with teacher id: {}", id);
        return lectures;
    }

    public List<Lecture> getLecturesByGroupId(Long id) {
        LOGGER.debug("Getting lectures with group id: {}", id);
        List<Lecture> lectures = this.jdbcTemplate.query("SELECT * FROM lectures WHERE group_id = ?", new LectureRowMapper(), id);
        LOGGER.debug("Getting lectures with group id: {}", id);
        return lectures;
    }
}
