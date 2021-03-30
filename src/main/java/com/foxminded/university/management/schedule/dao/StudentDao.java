package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentDao extends AbstractDao<Student> implements Dao<Student, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDao.class);
    private final EntityManager entityManager;

    public StudentDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected Student create(Student student) {
        LOGGER.debug("Creating student: {}", student);
        entityManager.persist(student);
        entityManager.flush();
        LOGGER.info("Student created successful with id: {}", student.getId());
        return new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroup());
    }

    @Override
    protected Student update(Student student) {
        LOGGER.debug("Updating student: {}", student);
        entityManager.merge(student);
        LOGGER.info("Student updated successful: {}", student);
        return new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                student.getCourseNumber(), student.getGroup());
    }

    @Override
    public Optional<Student> getById(Long id) {
        LOGGER.debug("Getting student by id: {}", id);
        Student student;
        try {
            student = entityManager.createQuery("select s from Student s join fetch s.group where s.id =: id", Student.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        LOGGER.info("Received student by id: {}. Received student: {}", id, student);
        return student != null ? Optional.of(student) : Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        LOGGER.debug("Getting all students");
        List<Student> students = entityManager.createQuery("select s from Student s join fetch s.group g join fetch g.faculty", Student.class).getResultList();
        LOGGER.info("Students received successful");
        return students;
    }

    @Override
    public boolean deleteById(Long id) {
        LOGGER.debug("Deleting student with id: {}", id);
        Student student = entityManager.find(Student.class, id);
        if (student == null) {
            return false;
        }
        entityManager.remove(student);
        LOGGER.info("Successful deleted student with id: {}", id);
        return true;
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        LOGGER.debug("Saving students: {}", students);
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            result.add(save(student));
        }
        LOGGER.info("Successful saved all students");
        return result;
    }
}
