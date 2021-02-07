package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
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
@WebMvcTest(GroupController.class)
class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupServiceImpl groupService;

    @Test
    public void shouldReturnViewWithAllGroups() throws Exception {
        List<Group> groups = List.of(
                new Group(1L, "AB-01", 1L, 1L),
                new Group(2L, "CD-21", 1L, 1L));

        when(groupService.getAllAGroups()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attribute("groups", groups));
    }

    @Test
    public void shouldReturnViewWithOneGroup() throws Exception {
        Group group = new Group(1L, "AB-01", 1L, 1L);
        when(groupService.getGroupById(1L)).thenReturn(group);

        mockMvc.perform(get("/groups/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attribute("group", group));
    }
}
