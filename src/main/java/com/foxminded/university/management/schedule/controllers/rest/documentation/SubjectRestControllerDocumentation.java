package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.subject.BaseSubjectDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectAddDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Subject;
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

@Tag(name = "subjects")
@SecurityRequirement(name = "bearer key")
public interface SubjectRestControllerDocumentation {
    @Operation(summary = "Get all subjects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All subjects were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Subject.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all subjects",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))})})
    ResponseEntity<List<Subject>> getAllSubjects();

    @Operation(summary = "Get subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found subject by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseSubjectDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get subject by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Subject is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseSubjectDto> getSubjectById(@PathVariable("id") Long id);

    @Operation(summary = "Delete subject by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete subject by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Subject is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteSubject(@PathVariable("id") Long id);

    @Operation(summary = "Add a new subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Subject.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new subject",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "409", description = "Subject with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Subject> addSubject(@Valid @RequestBody SubjectAddDto subjectAddDto);

    @Operation(summary = "Update an existing subject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject was updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Audience.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing subject",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Subject is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Subject with this name is already exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Subject> updateSubject(@Valid @RequestBody SubjectUpdateDto subjectUpdateDto, @PathVariable("id") Long id);

}
