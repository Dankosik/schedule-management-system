package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.ScheduleRowMapper;
import com.foxminded.university.management.schedule.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ScheduleDao extends AbstractDao<Schedule> implements Dao<Schedule, Long> {
    private final JdbcTemplate jdbcTemplate;

    public ScheduleDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Schedule create(Schedule schedule) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("university_id", schedule.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Schedule(newId.longValue(), schedule.getUniversityId());
    }

    @Override
    protected Schedule update(Schedule schedule) {
        this.jdbcTemplate.update("UPDATE schedule SET university_id = ? WHERE id = ?", schedule.getUniversityId(), schedule.getId());
        return new Schedule(schedule.getId(), schedule.getUniversityId());
    }

    @Override
    public Optional<Schedule> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM schedule WHERE id = ?", new ScheduleRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Schedule> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM schedule", new ScheduleRowMapper());
    }

    @Override
    public boolean deleteById(Long id) {
        return this.jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", id) == 1;
    }

    @Override
    public List<Schedule> saveAll(List<Schedule> schedules) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            result.add(save(schedule));
        }
        return result;
    }

    public List<Schedule> getSchedulesByUniversityId(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM schedule WHERE university_id = ?", new ScheduleRowMapper(), id);
    }
}
