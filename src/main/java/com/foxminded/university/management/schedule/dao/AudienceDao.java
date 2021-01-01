package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

@Component
public class AudienceDao extends AbstractDao<Audience> implements Dao<Audience> {
     JdbcTemplate jdbcTemplate;

    private final RowMapper<Audience> audienceRowMapper = (resultSet, rowColumn) -> {
        Audience audience = new Audience();
        audience.setId(resultSet.getLong("id"));
        audience.setNumber(resultSet.getInt("number"));
        audience.setCapacity(resultSet.getInt("capacity"));
        audience.setUniversityId(resultSet.getLong("university_id"));
        return audience;
    };

    @Autowired
    public AudienceDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
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
        return new Audience((long) newId.intValue(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
    }

    @Override
    protected Audience update(Audience audience) {
        this.jdbcTemplate.update("UPDATE audiences SET number = ?, capacity = ?,  university_id = ? WHERE id = ?",
                audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
        return new Audience(audience.getId(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
    }

    @Override
    public Optional<Audience> getById(Long id) {
        return this.jdbcTemplate.query("SELECT * FROM audiences WHERE id = ?", audienceRowMapper, new Object[]{id})
                .stream().findAny();
    }

    @Override
    public List<Audience> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM audiences", audienceRowMapper);
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
