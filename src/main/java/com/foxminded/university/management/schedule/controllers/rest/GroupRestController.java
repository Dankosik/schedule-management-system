package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.group.BaseGroupDto;
import com.foxminded.university.management.schedule.dto.group.GroupAddDto;
import com.foxminded.university.management.schedule.dto.group.GroupDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.GroupDtoUtils;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/groups")
public class GroupRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupRestController.class);
    private final GroupServiceImpl groupService;
    private final FacultyServiceImpl facultyService;

    public GroupRestController(GroupServiceImpl groupService, FacultyServiceImpl facultyService) {
        this.groupService = groupService;
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups() {
        return new ResponseEntity<>(groupService.getAllGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGroupById(@PathVariable("id") Long id) {
        Group group = groupService.getGroupById(id);
        BaseGroupDto groupDto = new BaseGroupDto();
        BeanUtils.copyProperties(group, groupDto);
        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addGroup(@Valid @RequestBody GroupAddDto groupAddDto) {
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfGroupFieldsHasErrors(groupAddDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        Group group = GroupDtoUtils.mapGroupDtoOnGroup(groupAddDto);
        groupService.saveGroup(group);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGroup(@Valid @RequestBody GroupUpdateDto groupUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(groupUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    groupUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfGroupFieldsHasErrors(groupUpdateDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();

        if (!groupService.isGroupWithIdExist(id)) {
            LOGGER.warn("Group with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Group with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        Group group = GroupDtoUtils.mapGroupDtoOnGroup(groupUpdateDto);
        groupService.saveGroup(group);
        return ResponseEntity.ok(group);
    }

    private Optional<ResponseEntity<Object>> getErrorResponseEntityIfGroupFieldsHasErrors(GroupDto groupDto) {
        if (!facultyService.isFacultyWithIdExist(groupDto.getFaculty().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Faculty with id: " + groupDto.getFaculty().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!GroupDtoUtils.isSuchFacultyFromGroupDtoExist(groupDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such faculty does not exist", HttpStatus.NOT_FOUND));
        return Optional.empty();
    }
}
