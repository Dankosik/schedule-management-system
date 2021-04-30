package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.teacher.BaseTeacherDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherAddDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.models.Teacher;
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

@Tag(name = "teachers")
@SecurityRequirement(name = "bearer key")
public interface TeacherRestControllerDocumentation {
    @Operation(summary = "Get all teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All teachers were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Teacher.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all teachers",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),})
    ResponseEntity<List<Teacher>> getAllTeachers();

    @Operation(summary = "Get teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found teacher by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseTeacherDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get teacher by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseTeacherDto> getTeacherById(@PathVariable("id") Long id);

    @Operation(summary = "Delete teacher by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete teacher by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long id);

    @Operation(summary = "Add a new teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Teacher.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new teacher",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher faculty is not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Teacher> addTeacher(@Valid @RequestBody TeacherAddDto teacherAddDto);

    @Operation(summary = "Update an existing teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher was updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Teacher.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing teacher",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Teacher is not found, or Teacher faculty not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Teacher> updateTeacher(@Valid @RequestBody TeacherUpdateDto teacherUpdateDto, @PathVariable("id") Long id);
}
