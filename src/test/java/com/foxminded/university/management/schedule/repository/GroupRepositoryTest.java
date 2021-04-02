package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class GroupRepositoryTest extends BaseDaoTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GroupRepository groupRepository;
    private Faculty faculty;
    private List<Student> students;
    private List<Lecture> lectures;

    @BeforeEach
    void setUp() {
        faculty = entityManager.find(Group.class, 1000L).getFaculty();
        students = entityManager.find(Group.class, 1000L).getStudents();
        lectures = entityManager.find(Group.class, 1000L).getLectures();
    }

    @Test
    void shouldReturnGroupWithIdOne() {
        Group actual = groupRepository.findById(1000L).get();
        Group expected = new Group(1000L, "AB-91", faculty, students, lectures);

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfGroups() {
        List<Group> expected = List.of(
                new Group(1000L, "AB-91", faculty, students, lectures),
                new Group(1001L, "BC-01", entityManager.find(Group.class, 1001L).getFaculty(),
                        entityManager.find(Group.class, 1001L).getStudents(), entityManager.find(Group.class, 1001L).getLectures()));
        List<Group> actual = groupRepository.findAll();

        assertTrue(actual.containsAll(expected));
    }
}
