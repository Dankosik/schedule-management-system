package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.lesson.BaseLessonDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonAddDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.models.Lesson;
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

@Tag(name = "lessons")
@SecurityRequirement(name = "bearer key")
public interface LessonRestControllerDocumentation {
    @Operation(summary = "Get all lessons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All lessons were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lesson.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all lessons",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))})})
    ResponseEntity<List<Lesson>> getAllLessons();

    @Operation(summary = "Get lesson by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found lesson by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseLessonDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get lesson by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lesson is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseLessonDto> getLessonById(@PathVariable("id") Long id);

    @Operation(summary = "Delete lesson by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete lesson by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lesson is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteLesson(@PathVariable("id") Long id);

    @Operation(summary = "Add a new lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new lesson",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lesson subject does not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Lesson> addLesson(@Valid @RequestBody LessonAddDto lessonAddDto);

    @Operation(summary = "Update an existing lesson")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lesson was updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing lesson",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lesson is not found, or Lesson subject does not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Lesson> updateLesson(@Valid @RequestBody LessonUpdateDto lessonUpdateDto, @PathVariable("id") Long id);
}
