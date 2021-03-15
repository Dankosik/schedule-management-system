package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AudienceDao extends AbstractDao<Audience> implements Dao<Audience, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudienceDao.class);
    private final EntityManager entityManager;

    public AudienceDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Audience create(Audience audience) {
        LOGGER.debug("Creating audience: {}", audience);
        try {
            entityManager.persist(audience);
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to create audience with id: " + audience.getId() +
                    ". Audience with number: " + audience.getNumber() + " is already exist");
        }
        LOGGER.info("Audience created successful: {}", audience.getId());
        return new Audience(audience.getId(), audience.getNumber(), audience.getCapacity());
    }

    @Override
    protected Audience update(Audience audience) {
        LOGGER.debug("Updating audience: {}", audience);
        try {
            entityManager.merge(audience);
            entityManager.flush();
            LOGGER.info("Audience updated successful: {}", audience);
            return new Audience(audience.getId(), audience.getNumber(), audience.getCapacity());
        } catch (PersistenceException e) {
            throw new DuplicateKeyException("Impossible to update audience with id: " + audience.getId() +
                    ". Audience with number: " + audience.getNumber() + " is already exist");
        }
    }

    @Override
    public Optional<Audience> getById(Long id) {
        LOGGER.debug("Getting audience by id: {}", id);
        Audience audience = entityManager.find(Audience.class, id);
        LOGGER.info("Received audience by id: {}. Received audience: {}", id, audience);
        return audience != null ? Optional.of(audience) : Optional.empty();
    }

    @Override
    public List<Audience> getAll() {
        LOGGER.debug("Getting all audiences");
        List<Audience> audiences = entityManager.createQuery("from Audience", Audience.class).getResultList();
        LOGGER.info("Audiences received successful");
        return audiences;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting audience with id: {}", id);
        Audience audience = entityManager.find(Audience.class, id);
        if (audience == null) {
            return false;
        }
        entityManager.remove(audience);
        LOGGER.info("Successful deleted audience with id: {}", id);
        return true;
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
}
