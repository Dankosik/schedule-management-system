package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.group.BaseGroupDto;
import com.foxminded.university.management.schedule.dto.group.GroupAddDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.models.Group;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "groups")
@SecurityRequirement(name = "bearer key")
public interface GroupRestControllerDocumentation {
    @Operation(summary = "Get all groups")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All groups were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Group.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all groups",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))})})
    ResponseEntity<List<Group>> getAllGroups();

    @Operation(summary = "Get group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found group by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseGroupDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get group by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Group is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseGroupDto> getGroupById(@PathVariable("id") Long id);

    @Operation(summary = "Delete group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete group by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Group is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id);

    @Operation(summary = "Add a new group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new group",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Group faculty is not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Group with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Group> addGroup(@Valid @RequestBody GroupAddDto groupAddDto);

    @Operation(summary = "Update an existing group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group was updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Group.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing group",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Group is not found, or Group faculty does not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Group with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Group> updateGroup(@Valid @RequestBody GroupUpdateDto groupUpdateDto, @PathVariable("id") Long id);
}
