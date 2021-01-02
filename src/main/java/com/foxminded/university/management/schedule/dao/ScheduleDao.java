package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.ScheduleRowMapper;
import com.foxminded.university.management.schedule.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ScheduleDao extends AbstractDao<Schedule> implements Dao<Schedule> {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ScheduleDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected Schedule create(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO schedule (id) values(?)");
            preparedStatement.setLong(1, schedule.getId());
            return preparedStatement;
        }, keyHolder);
        return new Schedule((Long) keyHolder.getKey());
    }

    @Override
    protected Schedule update(Schedule schedule) {
        //TODO
        return null;
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
    public boolean delete(Schedule schedule) {
        return this.jdbcTemplate.update("DELETE FROM schedule WHERE id = ?", schedule.getId()) == 1;
    }

    @Override
    public List<Schedule> saveAll(List<Schedule> schedules) {
        List<Schedule> result = new ArrayList<>();
        for (Schedule schedule : schedules) {
            result.add(save(schedule));
        }
        return result;
    }
}
