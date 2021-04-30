package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.faculty.BaseFacultyDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyAddDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
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

@Tag(name = "faculties")
@SecurityRequirement(name = "bearer key")
public interface FacultyRestControllerDocumentation {
    @Operation(summary = "Get all faculties")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All faculties were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Faculty.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all faculties",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))})})
    ResponseEntity<List<Faculty>> getAllFaculties();

    @Operation(summary = "Get faculty by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found faculty by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseFacultyDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get faculty by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Faculty is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseFacultyDto> getFacultyById(@PathVariable("id") Long id);

    @Operation(summary = "Delete faculty by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faculty was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete faculty by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Faculty is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteFaculty(@PathVariable("id") Long id);

    @Operation(summary = "Add a new faculty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faculty was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Faculty.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new faculty",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "409", description = "Faculty with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Faculty> addFaculty(@Valid @RequestBody FacultyAddDto facultyAddDto);


    @Operation(summary = "Update an existing faculty")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Faculty was updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Faculty.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing faculty",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Faculty is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Faculty with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Faculty> updateFaculty(@Valid @RequestBody FacultyUpdateDto facultyUpdateDto, @PathVariable("id") Long id);
}
