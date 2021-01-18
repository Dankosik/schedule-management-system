package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.exceptions.AudienceServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        verify(audienceDao, times(1)).getAll();
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
        verify(audienceDao, times(4)).getAll();
    }

    @Test
    void shouldAddLectureToAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));
        when(lectureService.saveLecture(new Lecture(1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L)))
                .thenReturn(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                        1L, 2L, 3L));

        Lecture actual = audienceService.addLectureToAudience(lecture, audience);
        assertEquals(new Lecture(1L, 1, Date.valueOf(LocalDate.of(2020, 1, 1)),
                1L, 2L, 3L), actual);

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(1)).saveLecture(lecture);
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
    void shouldThrowExceptionIfAudienceWithNumberAlreadyExist() {
        when(audienceDao.getAll()).thenReturn(List.of(audience));

        assertThrows(AudienceServiceException.class, ()->audienceService.saveAudience(audience));

        verify(audienceDao, times(1)).getAll();
        verify(audienceDao, never()).save(audience);
    }

    @Test
    void shouldThrowExceptionIfAudienceWithIdNotFound() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(AudienceServiceException.class, ()->audienceService.getAudienceById(1L));

        verify(audienceDao, times(1)).getById(1L);
        verify(audienceDao, never()).save(audience);
    }

    @Test
    void shouldThrowExceptionIfAudienceNotPresentInAddingLectureToAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(AudienceServiceException.class, ()->audienceService.addLectureToAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(0)).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInAddingLectureToAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(AudienceServiceException.class, ()->audienceService.addLectureToAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(0)).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfAudienceNotPresentInRemovingLectureFromAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.empty());
        when(lectureDao.getById(1L)).thenReturn(Optional.of(lecture));

        assertThrows(AudienceServiceException.class, ()->audienceService.removeLectureFromAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(0)).saveLecture(lecture);
    }

    @Test
    void shouldThrowExceptionIfLectureNotPresentInRemovingLectureFromAudience() {
        when(audienceDao.getById(1L)).thenReturn(Optional.of(audience));
        when(lectureDao.getById(1L)).thenReturn(Optional.empty());

        assertThrows(AudienceServiceException.class, ()->audienceService.removeLectureFromAudience(lecture, audience));

        verify(audienceDao, times(1)).getById(1L);
        verify(lectureDao, times(1)).getById(1L);
        verify(lectureService, times(0)).saveLecture(lecture);
    }
}
