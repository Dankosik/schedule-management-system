package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AudienceServiceImpl.class})
class AudienceServiceImplTest {
    private final Audience audience = new Audience(1L, 202, 45, null);
    private final List<Audience> audiences = List.of(audience,
            new Audience(2L, 203, 50, null),
            new Audience(3L, 204, 55, null),
            new Audience(4L, 205, 60, null));
    @Autowired
    private AudienceServiceImpl audienceService;
    @MockBean
    private AudienceDao audienceDao;

    @Test
    void shouldSaveAudience() {
        when(audienceDao.save(new Audience(202, 45, null))).thenReturn(audience);
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
        when(audienceDao.save(new Audience(202, 45, null))).thenReturn(audience);
        when(audienceDao.save(new Audience(203, 50, null))).thenReturn(audiences.get(1));
        when(audienceDao.save(new Audience(204, 55, null))).thenReturn(audiences.get(2));
        when(audienceDao.save(new Audience(205, 60, null))).thenReturn(audiences.get(3));

        List<Audience> actual = audienceService.saveAllAudiences(audiences);

        assertEquals(audiences, actual);

        verify(audienceDao, times(1)).save(audiences.get(0));
        verify(audienceDao, times(1)).save(audiences.get(1));
        verify(audienceDao, times(1)).save(audiences.get(2));
        verify(audienceDao, times(1)).save(audiences.get(3));
    }

    @Test
    void shouldThrowExceptionIfCreatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(202, 45, null);

        when(audienceDao.save(expected)).thenThrow(DuplicateKeyException.class);

        assertThrows(ServiceException.class, () -> audienceService.saveAudience(expected));

        verify(audienceDao, times(1)).save(expected);
    }

    @Test
    void shouldThrowExceptionIfUpdatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(1L, 202, 45, null);

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
    void shouldReturnAudienceNumbersForEachAudiences() {
        List<Audience> input = List.of(new Audience(1L, 202, 45, null),
                new Audience(2L, 203, 50, null),
                new Audience(3L, 204, 55, null));

        List<Integer> expected = List.of(202, 203, 204);

        assertEquals(expected, audienceService.getAudienceNumbersWithPossibleNullForAudiences(input));
    }

    @Test
    void shouldReturnAudienceNumbersForEachAudiencesWithIdNull() {
        List<Audience> input = Arrays.asList(null, new Audience(2L, 203, 50, null), null);

        List<Integer> expected = Arrays.asList(null, 203, null);

        assertEquals(expected, audienceService.getAudienceNumbersWithPossibleNullForAudiences(input));
    }

    @Test
    void shouldReturnAudiencesForLectures() {
        List<Audience> expected = List.of(new Audience(1L, 202, 45, null),
                new Audience(2L, 203, 50, null),
                new Audience(3L, 204, 55, null));

        List<Lecture> input = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(1L, 202, 45, null), null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(2L, 203, 50, null), null, null, null),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(3L, 204, 55, null), null, null, null));

        assertEquals(expected, audienceService.getAudiencesWithPossibleNullForLectures(input));
    }

    @Test
    void shouldReturnAudiencesForEachLecturesWithIdNull() {
        List<Audience> expected = Arrays.asList(new Audience(1L, 202, 45, null), null,
                new Audience(3L, 204, 55, null));

        List<Lecture> input = Arrays.asList(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(1L, 202, 45, null), null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(3L, 204, 55, null), null, null, null));

        assertEquals(expected, audienceService.getAudiencesWithPossibleNullForLectures(input));
    }
}
