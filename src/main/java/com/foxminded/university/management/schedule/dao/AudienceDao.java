package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.AudienceRowMapper;
import com.foxminded.university.management.schedule.models.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AudienceDao extends AbstractDao<Audience> implements Dao<Audience, Long> {
    private final Logger LOGGER = LoggerFactory.getLogger(AudienceDao.class);
    private final JdbcTemplate jdbcTemplate;

    public AudienceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Audience create(Audience audience) {
        LOGGER.debug("Creating audience: {}", audience);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("audiences").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("number", audience.getNumber());
        params.put("capacity", audience.getCapacity());
        params.put("university_id", audience.getUniversityId());

        Number newId;
        try {
            newId = simpleJdbcInsert.executeAndReturnKey(params);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create audience with id: " + audience.getId() +
                    ". Audience with number: " + audience.getNumber() + " is already exist");
        }
        LOGGER.info("Audience created successful with id: {}", newId);
        return new Audience(newId.longValue(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
    }

    @Override
    protected Audience update(Audience audience) {
        LOGGER.debug("Updating audience: {}", audience);
        try {
            this.jdbcTemplate.update("UPDATE audiences SET number = ?, capacity = ?,  university_id = ? WHERE id = ?",
                    audience.getNumber(), audience.getCapacity(), audience.getUniversityId(), audience.getId());
            LOGGER.info("Audience updated successful: {}", audience);
            return new Audience(audience.getId(), audience.getNumber(), audience.getCapacity(), audience.getUniversityId());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to update audience with id: " + audience.getId() +
                    ". Audience with number: " + audience.getNumber() + " is already exist");
        }
    }

    @Override
    public Optional<Audience> getById(Long id) {
        LOGGER.debug("Getting audience by id: {}", id);
        Optional<Audience> audience = this.jdbcTemplate.query("SELECT * FROM audiences WHERE id = ?", new AudienceRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Successful received audience by id: {}. Received audience: {}", id, audience);
        return audience;
    }

    @Override
    public List<Audience> getAll() {
        LOGGER.debug("Getting all audiences");
        List<Audience> audiences = this.jdbcTemplate.query("SELECT * FROM audiences", new AudienceRowMapper());
        LOGGER.info("Audiences received successful");
        return audiences;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting audience with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM audiences WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted audience with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Audience> saveAll(List<Audience> audiences) {
        LOGGER.debug("Saving audiences: {}", audiences);
        List<Audience> result = new ArrayList<>();
        for (Audience audience : audiences) {
            result.add(save(audience));
        }
        LOGGER.info("Successful saved all audiences");
        return result;
    }

    public List<Audience> getAudiencesByUniversityId(Long id) {
        LOGGER.debug("Getting audiences with university id: {}", id);
        List<Audience> audiences = this.jdbcTemplate.query("SELECT * FROM audiences WHERE university_id = ?", new AudienceRowMapper(), id);
        LOGGER.info("Successful received audiences with university id: {}", id);
        return audiences;
    }
}
