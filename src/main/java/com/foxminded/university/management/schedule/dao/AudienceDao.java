package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.AudienceRowMapper;
import com.foxminded.university.management.schedule.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

@Component
public class AudienceDao extends AbstractDao<Audience> implements Dao<Audience> {
    private final JdbcTemplate jdbcTemplate;

    public AudienceDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public AudienceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Audience create(Audience audience) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("audiences").usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("number", audience.getNumber());
        params.put("capacity", audience.getCapacity());
        params.put("university_id", audience.getUniversityId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(params);
        return new Audience(newId.longValue(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
    }

    @Override
    protected Audience update(Audience audience) {
        this.jdbcTemplate.update("UPDATE audiences SET number = ?, capacity = ?,  university_id = ? WHERE id = ?",
                audience.getNumber(), audience.getCapacity(), audience.getUniversityId(), audience.getId());
        return new Audience(audience.getId(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
    }

    @Override
    public Optional<Audience> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM audiences WHERE id = ?", new AudienceRowMapper(), new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Audience> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM audiences", new AudienceRowMapper());
    }

    @Override
    public boolean delete(Audience audience) {
        return this.jdbcTemplate.update("DELETE FROM audiences WHERE id = ?", audience.getId()) == 1;
    }

    @Override
    public List<Audience> saveAll(List<Audience> audiences) {
        List<Audience> result = new ArrayList<>();
        for (Audience audience : audiences) {
            result.add(save(audience));
        }
        return result;
    }
}
