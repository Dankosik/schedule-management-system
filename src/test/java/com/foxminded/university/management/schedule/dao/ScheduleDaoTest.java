package com.foxminded.university.management.schedule.dao;

import com.foxminded.university.management.schedule.models.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScheduleDaoTest extends BaseDaoTest {
    private ScheduleDao scheduleDao;

    @BeforeEach
    void setUp() {
        scheduleDao = new ScheduleDao(jdbcTemplate);
    }

    @Test
    void shouldCreateNewSchedule() {
        Schedule schedule = new Schedule(1000L);
        Long scheduleId = scheduleDao.save(schedule).getId();
        assertTrue(testUtils.existsById("schedule", scheduleId));

        Map<String, Object> map = testUtils.getEntry("schedule", scheduleId);
        Schedule actual = new Schedule((Long) map.get("university_id"));
        assertEquals(schedule, actual);
    }

    @Test
    void shouldUpdateSchedule() {
        Schedule schedule = new Schedule(1000L, 1000L);
        Long scheduleId = scheduleDao.save(schedule).getId();
        assertTrue(testUtils.existsById("schedule", scheduleId));

        Map<String, Object> map = testUtils.getEntry("schedule", scheduleId);
        Schedule actual = new Schedule((Long) map.get("id"), (Long) map.get("university_id"));
        assertEquals(schedule, actual);
    }

    @Test
    void shouldReturnScheduleWithIdOne() {
        Map<String, Object> map = testUtils.getEntry("schedule", 1000L);
        Schedule expected = new Schedule((Long) map.get("id"), (Long) map.get("university_id"));
        Schedule actual = scheduleDao.getById(1000L).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnListOfSchedules() {
        List<Schedule> expected = List.of(
                new Schedule(1000L, 1000L),
                new Schedule(1001L, 1000L));
        List<Schedule> actual = scheduleDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldDeleteSchedule() {
        assertTrue(scheduleDao.deleteById(1000L));
        assertFalse(testUtils.existsById("schedule", 1000L));
    }

    @Test
    void shouldSaveListOfSchedules() {
        List<Schedule> audiences = List.of(
                new Schedule(1000L),
                new Schedule(1001L));

        List<Schedule> expected = List.of(
                new Schedule(1000L, 1000L),
                new Schedule(1001L, 1000L),
                new Schedule(1L, 1000L),
                new Schedule(2L, 1001L));
        scheduleDao.saveAll(audiences);
        List<Schedule> actual = scheduleDao.getAll();

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldReturnListOfSchedulesWithUniversityIdOne() {
        List<Schedule> expected = List.of(
                new Schedule(1000L, 1000L),
                new Schedule(1001L, 1000L));
        List<Schedule> actual = scheduleDao.getSchedulesByUniversityId(1000L);

        assertTrue(actual.containsAll(expected));
    }

    @Test
    void shouldNotFindScheduleNotExist() {
        assertFalse(scheduleDao.getById(21L).isPresent());
    }

    @Test
    void shouldReturnFalseIfScheduleNotExist() {
        assertFalse(() -> scheduleDao.deleteById(21L));
    }
}