package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class GroupFormatter implements Formatter<Group> {
    private final GroupServiceImpl groupService;

    public GroupFormatter(GroupServiceImpl groupService) {
        this.groupService = groupService;
    }

    @Override
    public Group parse(String s, Locale locale) throws ParseException {
        return groupService.getGroupById(Long.valueOf(s));
    }

    @Override
    public String print(Group group, Locale locale) {
        return group.toString();
    }
}
