package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LectureDao extends AbstractDao<Lecture> implements Dao<Lecture, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudienceDao.class);
    private final EntityManager entityManager;

    public LectureDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Lecture create(Lecture lecture) {
        LOGGER.debug("Creating lecture: {}", lecture);
        entityManager.persist(lecture);
        LOGGER.info("Lecture created successful with id: {}", lecture.getId());
        return new Lecture(lecture.getId(), lecture.getNumber(), lecture.getDate(), lecture.getAudience(),
                lecture.getGroup(), lecture.getLesson(), lecture.getTeacher());
    }

    @Override
    protected Lecture update(Lecture lecture) {
        LOGGER.debug("Updating lecture: {}", lecture);
        entityManager.merge(lecture);
        LOGGER.info("Lecture updated successful: {}", lecture);
        return new Lecture(lecture.getId(), lecture.getNumber(), lecture.getDate(), lecture.getAudience(),
                lecture.getGroup(), lecture.getLesson(), lecture.getTeacher());
    }

    @Override
    public Optional<Lecture> getById(Long id) {
        LOGGER.debug("Getting lecture by id: {}", id);
        Lecture lecture = entityManager.find(Lecture.class, id);
        LOGGER.info("Received lecture by id: {}. Received lecture: {}", id, lecture);
        return lecture != null ? Optional.of(lecture) : Optional.empty();
    }

    @Override
    public List<Lecture> getAll() {
        LOGGER.debug("Getting all lectures");
        List<Lecture> lectures = entityManager.createQuery("select lecture from  Lecture lecture " +
                "left join fetch lecture.group g left join fetch g.faculty left join fetch lecture.audience left join fetch lecture.lesson le " +
                "left join fetch le.subject left join fetch lecture.teacher t left join fetch t.faculty", Lecture.class).getResultList();
        LOGGER.info("Lectures received successful");
        return lectures;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting lecture with id: {}", id);
        Lecture lecture = entityManager.find(Lecture.class, id);
        if (lecture == null) {
            return false;
        }
        entityManager.remove(lecture);
        LOGGER.info("Successful deleted lecture with id: {}", id);
        return true;
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
}
