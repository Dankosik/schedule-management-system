package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lesson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LessonDao extends AbstractDao<Lesson> implements Dao<Lesson, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonDao.class);
    private final EntityManager entityManager;

    public LessonDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Lesson create(Lesson lesson) {
        LOGGER.debug("Creating lesson: {}", lesson);
        entityManager.persist(lesson);
        LOGGER.info("Lesson created successful with id: {}", lesson.getId());
        return new Lesson(lesson.getId(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(),
                lesson.getSubject(), lesson.getLectures());
    }

    @Override
    protected Lesson update(Lesson lesson) {
        LOGGER.debug("Updating lesson: {}", lesson);
        entityManager.merge(lesson);
        entityManager.flush();
        return new Lesson(lesson.getId(), lesson.getNumber(), lesson.getStartTime(), lesson.getDuration(),
                lesson.getSubject(), lesson.getLectures());
    }


    @Override
    public Optional<Lesson> getById(Long id) {
        LOGGER.debug("Getting lesson by id: {}", id);
        Lesson lesson;
        try {
            lesson = entityManager.createQuery("select l from Lesson l left join fetch l.subject where l.id =: id", Lesson.class).setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        LOGGER.info("Received lesson by id: {}. Received lesson: {}", id, lesson);
        return lesson != null ? Optional.of(lesson) : Optional.empty();
    }

    @Override
    public List<Lesson> getAll() {
        LOGGER.debug("Getting all lessons");
        List<Lesson> lessons = entityManager.createQuery("select l from Lesson l left join fetch l.subject", Lesson.class).getResultList();
        LOGGER.info("Lessons received successful");
        return lessons;

    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting lesson with id: {}", id);
        Lesson lesson = entityManager.find(Lesson.class, id);
        if (lesson == null) {
            return false;
        }
        entityManager.remove(lesson);
        LOGGER.info("Successful deleted lesson with id: {}", id);
        return true;
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
}
