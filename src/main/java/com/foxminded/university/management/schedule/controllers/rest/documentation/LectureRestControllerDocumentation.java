package com.foxminded.university.management.schedule.controllers.rest.documentation;

import com.foxminded.university.management.schedule.controllers.rest.documentation.schemas.ForbiddenErrorSchema;
import com.foxminded.university.management.schedule.controllers.rest.errors.ErrorRequestBuilder;
import com.foxminded.university.management.schedule.dto.lecture.BaseLectureDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureAddDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureUpdateDto;
import com.foxminded.university.management.schedule.models.Lecture;
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

@Tag(name = "lectures")
@SecurityRequirement(name = "bearer key")
public interface LectureRestControllerDocumentation {
    @Operation(summary = "Get all lectures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All lectures were found",
                    content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Lecture.class)))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get all lectures",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))})})
    ResponseEntity<List<Lecture>> getAllLectures();

    @Operation(summary = "Get lecture by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found lecture by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BaseLectureDto.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to get lecture by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lecture not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<BaseLectureDto> getLectureById(@PathVariable("id") Long id);

    @Operation(summary = "Delete lecture by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lecture was deleted"),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to delete lecture by id",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lecture is not found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Void> deleteLecture(@PathVariable("id") Long id);

    @Operation(summary = "Add a new lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lecture was added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Lecture.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to add a new lecture",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lecture audience, group, lesson, teacher does not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Lesson number and lecture number should be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Lecture> addLecture(@Valid @RequestBody LectureAddDto lectureAddDto);

    @Operation(summary = "Update an existing lecture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lecture updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Lecture.class))}),
            @ApiResponse(responseCode = "400", description = "Must not be unrecognized field, or need required field, or URI id and request id need to be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "401", description = "Need to authorize",
                    content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "403", description = "This role is not allowed to update an existing lecture",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ForbiddenErrorSchema.class))}),
            @ApiResponse(responseCode = "404", description = "Lecture not found or Lecture audience, group, lesson, teacher does not exist",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))}),
            @ApiResponse(responseCode = "409", description = "Lesson number and lecture number should be the same",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorRequestBuilder.class))})})
    ResponseEntity<Lecture> updateLecture(@Valid @RequestBody LectureUpdateDto lectureUpdateDto, @PathVariable("id") Long id);
}
