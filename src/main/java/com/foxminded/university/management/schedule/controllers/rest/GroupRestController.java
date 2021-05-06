package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.documentation.GroupRestControllerDocumentation;
import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.group.BaseGroupDto;
import com.foxminded.university.management.schedule.dto.group.GroupAddDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.GroupDtoUtils;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/groups")
public class GroupRestController implements GroupRestControllerDocumentation {
    private final GroupService groupService;

    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseGroupDto> getGroupById(@PathVariable("id") Long id) {
        Group group = groupService.getGroupById(id);
        BaseGroupDto groupDto = new BaseGroupDto();
        BeanUtils.copyProperties(group, groupDto);
        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Group> addGroup(@Valid @RequestBody GroupAddDto groupAddDto) {
        Group group = GroupDtoUtils.mapGroupDtoOnGroup(groupAddDto);
        groupService.saveGroup(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody GroupUpdateDto groupUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(groupUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    groupUpdateDto.getId() + " should be the same");
        }
        if (!groupService.isGroupWithIdExist(id)) {
            throw new EntityNotFoundException("Group with id: " + id + " is not found");
        }

        Group group = GroupDtoUtils.mapGroupDtoOnGroup(groupUpdateDto);
        groupService.saveGroup(group);
        return ResponseEntity.ok(group);
    }
}
