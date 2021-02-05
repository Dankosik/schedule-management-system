package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AudienceController.class)
class AudienceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AudienceServiceImpl audienceService;

    @Test
    public void shouldReturnViewWithAllAudiences() throws Exception {
        List<Audience> audiences = List.of(
                new Audience(1L, 301, 45, 1L),
                new Audience(2L, 302, 55, 1L));

        when(audienceService.getAllAudiences()).thenReturn(audiences);

        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences"))
                .andExpect(model().attribute("audiences", audiences));
    }

    @Test
    public void shouldReturnViewWithOneAudience() throws Exception {
        Audience audience = new Audience(1L, 301, 45, 1L);
        when(audienceService.getAudienceById(1L)).thenReturn(audience);

        mockMvc.perform(get("/audiences/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("audience"))
                .andExpect(model().attribute("audience", audience));
    }
}
