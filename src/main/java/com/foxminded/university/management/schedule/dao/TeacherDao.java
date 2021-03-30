package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDao extends AbstractDao<Teacher> implements Dao<Teacher, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDao.class);
    private final EntityManager entityManager;

    public TeacherDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Teacher create(Teacher teacher) {
        LOGGER.debug("Creating teacher: {}", teacher);
        entityManager.persist(teacher);
        LOGGER.info("Teacher created successful with id: {}", teacher.getId());
        return new Teacher(teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFaculty(), teacher.getLectures());
    }

    @Override
    protected Teacher update(Teacher teacher) {
        LOGGER.debug("Updating teacher: {}", teacher);
        entityManager.merge(teacher);
        LOGGER.info("Teacher updated successful: {}", teacher);
        return new Teacher(teacher.getId(), teacher.getFirstName(), teacher.getLastName(), teacher.getMiddleName(),
                teacher.getFaculty(), teacher.getLectures());
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        LOGGER.debug("Getting teacher by id: {}", id);
        Teacher teacher;
        try {
            teacher = entityManager.createQuery("select t from Teacher t left join fetch t.faculty left join fetch t.lectures l " +
                    "left join fetch l.group g left join fetch g.faculty left join fetch l.audience left join fetch l.lesson le " +
                    "left join fetch le.subject where t.id =: id", Teacher.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        LOGGER.info("Received teacher by id: {}. Received teacher: {}", id, teacher);
        return teacher != null ? Optional.of(teacher) : Optional.empty();
    }

    @Override
    public List<Teacher> getAll() {
        LOGGER.debug("Getting all teachers");
        List<Teacher> teachers = entityManager.createQuery("select t from Teacher t join fetch t.faculty", Teacher.class).getResultList();
        LOGGER.info("Teachers received successful");
        return teachers;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting teacher with id: {}", id);
        Teacher teacher = entityManager.find(Teacher.class, id);
        if (teacher == null) {
            return false;
        }
        entityManager.remove(teacher);
        LOGGER.info("Successful deleted teacher with id: {}", id);
        return true;
    }

    @Override
    public List<Teacher> saveAll(List<Teacher> teachers) {
        LOGGER.debug("Saving teachers: {}", teachers);
        List<Teacher> result = new ArrayList<>();
        for (Teacher teacher : teachers) {
            result.add(save(teacher));
        }
        LOGGER.info("Successful saved all teachers");
        return result;
    }
}
