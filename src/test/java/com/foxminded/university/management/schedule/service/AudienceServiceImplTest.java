package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import com.foxminded.university.management.schedule.service.impl.LectureServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AudienceServiceImplTest {
    private final Audience audience = new Audience(1L, 202, 45, 1L);
    private final List<Audience> audiences = List.of(audience,
            new Audience(2L, 203, 50, 1L),
            new Audience(3L, 204, 55, 1L),
            new Audience(4L, 205, 60, 1L));

    private final Lecture lecture = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
            null, 2L, 3L);
    @Autowired
    private AudienceServiceImpl audienceService;
    @MockBean
    private AudienceDao audienceDao;
    @MockBean
    private LectureDao lectureDao;
    @MockBean
    private LectureServiceImpl lectureService;

    @Test
    void shouldSaveAudience() {
        when(audienceDao.save(new Audience(202, 45, 1L))).thenReturn(audience);
        Audience actual = audienceService.saveAudience(audience);

        assertEquals(audience, actual);

        verify(audienceDao, times(1)).save(audience);
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        Audience actual = audienceService.getAudienceById(1L);

        assertEquals(audience, actual);

        verify(audienceDao, times(2)).getById(1L);
    }

    @Test
    void shouldReturnListOfAudiences() {
        when(audienceDao.getAll()).thenReturn(audiences);

        assertEquals(audiences, audienceService.getAllAudiences());

        verify(audienceDao, times(1)).getAll();
    }

    @Test
    void shouldDeleteAudienceWithIdOne() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(audienceDao.deleteById(1L)).thenReturn(true);

        audienceService.deleteAudienceById(1L);

        verify(audienceDao, times(1)).deleteById(1L);
        verify(audienceDao, times(2)).getById(1L);
    }

    @Test
    void shouldSaveListOfAudiences() {
        when(audienceDao.save(new Audience(202, 45, 1L))).thenReturn(audience);
        when(audienceDao.save(new Audience(203, 50, 1L))).thenReturn(audiences.get(1));
        when(audienceDao.save(new Audience(204, 55, 1L))).thenReturn(audiences.get(2));
        when(audienceDao.save(new Audience(205, 60, 1L))).thenReturn(audiences.get(3));

        List<Audience> actual = audienceService.saveAllAudiences(audiences);

        assertEquals(audiences, actual);

        verify(audienceDao, times(1)).save(audiences.get(0));
        verify(audienceDao, times(1)).save(audiences.get(1));
        verify(audienceDao, times(1)).save(audiences.get(2));
        verify(audienceDao, times(1)).save(audiences.get(3));
    }

    @Test
    void shouldAddLectureToAudience() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L);

        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureService.saveLecture(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L))).thenReturn(expected);

        Lecture actual = audienceService.addLectureToAudience(lecture, audience);
        assertEquals(expected, actual);

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(1)).saveLecture(expected);
    }

    @Test
    void shouldRemoveLectureFromAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureService.saveLecture(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, 2L, 3L)))
                .thenReturn(lecture);

        Lecture actual = audienceService.removeLectureFromAudience(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L), audience);
        assertEquals(lecture, actual);

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(1)).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfCreatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(202, 45, 1L);

        when(audienceDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> audienceService.saveAudience(expected));

        verify(audienceDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfUpdatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(1L, 202, 45, 1L);

        when(audienceDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> audienceService.saveAudience(expected));

        verify(audienceDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfAudienceWithInputIdNotFound() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> audienceService.getAudienceById(1L));

        verify(audienceDao, times(1)).getById(1L);
        verify(audienceDao, never()).save(audience);
    }

    @Test
    void shouldThrowExceptionIfAudienceNotPresentInAddingLectureToAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> audienceService.addLectureToAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureService, never()).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInAddingLectureToAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> audienceService.addLectureToAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, never()).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfAudienceNotPresentInRemovingLectureFromAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(ServiceException.class, () -> audienceService.removeLectureFromAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, never()).getById(1L);
        verify(lectureService, never()).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInRemovingLectureFromAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> audienceService.removeLectureFromAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, never()).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureIsAlreadyAddedToAudience() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L);

        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> audienceService.addLectureToAudience(expected, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, never()).saveLecture(expected);
    }

    @Test
    void shouldThrowExceptionIfLectureIsAlreadyRemovedFromAudience() {
        Lecture expected = new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                null, 2L, 3L);

        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(expected));

        assertThrows(ServiceException.class, () -> audienceService.removeLectureFromAudience(expected, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, never()).saveLecture(expected);
    }

    @Test
    void shouldReturnAudienceNumbersForAudiences() {
        List<Audience> input = List.of(new Audience(1L, 202, 45, 1L),
                new Audience(2L, 203, 50, 1L),
                new Audience(3L, 204, 55, 1L));

        List<Integer> expected = List.of(202, 203, 204);

        assertEquals(expected, audienceService.getAudienceNumbersForAudiences(input));
    }

    @Test
    void shouldReturnAudiencesForLectures() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(new Audience(1L, 202, 45, 1L)));
        when(audienceDao.getById(2L)).thenReturn(Optional.of(new Audience(2L, 203, 50, 1L)));
        when(audienceDao.getById(3L)).thenReturn(Optional.of(new Audience(3L, 204, 55, 1L)));

        List<Audience> expected = List.of(new Audience(1L, 202, 45, 1L),
                new Audience(2L, 203, 50, 1L),
                new Audience(3L, 204, 55, 1L));

        List<Lecture> input = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)), 1L, 1L, 1L),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), 2L, 1L, 1L),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)), 3L, 1L, 1L));

        assertEquals(expected, audienceService.getAudiencesForLectures(input));

        verify(audienceDao, times(2)).getById(1L);
        verify(audienceDao, times(2)).getById(2L);
        verify(audienceDao, times(2)).getById(3L);
    }
}
