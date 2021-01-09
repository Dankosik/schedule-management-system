package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.LectureRowMapper;
import com.foxminded.university.management.schedule.models.Lecture;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LectureDao extends AbstractDao<Lecture> implements Dao<Lecture, Long> {
    private final JdbcTemplate jdbcTemplate;

    public LectureDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Lecture create(Lecture lecture) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("lectures").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("number", lecture.getNumber());
        params.put("date", lecture.getDate());
        params.put("audience_id", lecture.getAudienceId());
        params.put("lesson_id", lecture.getLessonId());
        params.put("teacher_id", lecture.getTeacherId());
        params.put("schedule_id", lecture.getScheduleId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Lecture(newId.longValue(), lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(),
                lecture.getLessonId(), lecture.getTeacherId(), lecture.getScheduleId());
    }

    @Override
    protected Lecture update(Lecture lecture) {
        this.jdbcTemplate.update("UPDATE lectures SET number = ?, date = ?,  audience_id = ?, lesson_id = ?, " +
                        "teacher_id = ?, schedule_id = ? WHERE id = ?",
                lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(), lecture.getLessonId(), lecture.getTeacherId(),
                lecture.getScheduleId(), lecture.getId());
        return new Lecture(lecture.getId(), lecture.getNumber(), lecture.getDate(), lecture.getAudienceId(),
                lecture.getLessonId(), lecture.getTeacherId(), lecture.getScheduleId());
    }

    @Override
    public Optional<Lecture> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lectures WHERE id = ?", new LectureRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Lecture> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM lectures", new LectureRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM lectures WHERE id = ?", id) == 1;
    }

    @Override
    public List<Lecture> saveAll(List<Lecture> lectures) {
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            result.add(save(lecture));
        }
        return result;
    }

    public List<Lecture> getLecturesByAudienceId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lectures WHERE audience_id = ?", new LectureRowMapper(), id);
    }

    public List<Lecture> getLecturesByLessonId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lectures WHERE lesson_id = ?", new LectureRowMapper(), id);
    }

    public List<Lecture> getLecturesByTeacherId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lectures WHERE teacher_id = ?", new LectureRowMapper(), id);
    }

    public List<Lecture> getLecturesByScheduleId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM lectures WHERE schedule_id = ?", new LectureRowMapper(), id);
    }
}
