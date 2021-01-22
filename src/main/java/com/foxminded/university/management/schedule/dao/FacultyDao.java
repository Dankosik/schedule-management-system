package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.dao.row_mappers.FacultyRowMapper;
import com.foxminded.university.management.schedule.models.Faculty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FacultyDao extends AbstractDao<Faculty> implements Dao<Faculty, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyDao.class);
    private final JdbcTemplate jdbcTemplate;

    public FacultyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected Faculty create(Faculty faculty) {
        LOGGER.debug("Creating faculty: {}", faculty);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate);
        simpleJdbcInsert.withTableName("faculties").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", faculty.getName());
        params.put("university_id", faculty.getUniversityId());

        Number newId;
        try {
            newId = simpleJdbcInsert.executeAndReturnKey(params);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to create faculty with id: " + faculty.getId() +
                    ". Faculty with name: " + faculty.getName() + " is already exist");
        }
        LOGGER.info("Faculty created successful with id: {}", newId);
        return new Faculty(newId.longValue(), faculty.getName(), faculty.getUniversityId());
    }

    @Override
    protected Faculty update(Faculty faculty) {
        LOGGER.debug("Updating faculty: {}", faculty);
        try {
            this.jdbcTemplate.update("UPDATE faculties SET name = ?, university_id = ? WHERE id = ?",
                    faculty.getName(), faculty.getUniversityId(), faculty.getId());
            LOGGER.info("Faculty updated successful: {}", faculty);
            return new Faculty(faculty.getId(), faculty.getName(), faculty.getUniversityId());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException("Impossible to update faculty with id: " + faculty.getId() +
                    ". Faculty with name: " + faculty.getName() + " is already exist");
        }
    }

    @Override
    public Optional<Faculty> getById(Long id) {
        LOGGER.debug("Getting faculty by id: {}", id);
        Optional<Faculty> faculty = this.jdbcTemplate.query("SELECT * FROM faculties WHERE id = ?", new FacultyRowMapper(), new Object[]{id})
                .stream().findAny();
        LOGGER.info("Successful received faculty by id: {}. Received faculty: {}", id, faculty);
        return faculty;
    }

    @Override
    public List<Faculty> getAll() {
        LOGGER.debug("Getting all faculties");
        List<Faculty> faculties = this.jdbcTemplate.query("SELECT * FROM faculties", new FacultyRowMapper());
        LOGGER.info("Audiences received successful");
        return faculties;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting faculty with id: {}", id);
        boolean isDeleted = this.jdbcTemplate.update("DELETE FROM faculties WHERE id = ?", id) == 1;
        LOGGER.info("Successful deleted faculty with id: {}", id);
        return isDeleted;
    }

    @Override
    public List<Faculty> saveAll(List<Faculty> faculties) {
        LOGGER.debug("Saving faculties: {}", faculties);
        List<Faculty> result = new ArrayList<>();
        for (Faculty faculty : faculties) {
            result.add(save(faculty));
        }
        LOGGER.info("Successful saved all faculties");
        return result;
    }

    public List<Faculty> getFacultiesByUniversityId(Long id) {
        LOGGER.debug("Getting faculties with university id: {}", id);
        List<Faculty> faculties = this.jdbcTemplate.query("SELECT * FROM faculties WHERE university_id = ?", new FacultyRowMapper(), id);
        LOGGER.info("Successful received faculties with university id: {}", id);
        return faculties;
    }
}
