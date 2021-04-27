package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.audience.AudienceAddDto;
import com.foxminded.university.management.schedule.dto.audience.AudienceUpdateDto;
import com.foxminded.university.management.schedule.dto.audience.BaseAudienceDto;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/audiences")
public class AudienceRestController {
    private final AudienceServiceImpl audienceService;

    public AudienceRestController(AudienceServiceImpl audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping
    public ResponseEntity<List<Audience>> getAllAudiences() {
        return new ResponseEntity<>(audienceService.getAllAudiences(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseAudienceDto> getAudienceById(@PathVariable("id") Long id) {
        Audience audience = audienceService.getAudienceById(id);
        BaseAudienceDto audienceDto = new BaseAudienceDto();
        BeanUtils.copyProperties(audience, audienceDto);
        return ResponseEntity.ok(audienceDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudience(@PathVariable("id") Long id) {
        audienceService.deleteAudienceById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Audience> addAudience(@Valid @RequestBody AudienceAddDto audienceAddDto) {
        Audience audience = new Audience();
        BeanUtils.copyProperties(audienceAddDto, audience);
        audienceService.saveAudience(audience);
        return new ResponseEntity<>(audience, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Audience> updateAudience(@Valid @RequestBody AudienceUpdateDto audienceUpdateDto, @PathVariable("id") Long id) {
        Audience audience = new Audience();
        BeanUtils.copyProperties(audienceUpdateDto, audience);

        if (!id.equals(audienceUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    audienceUpdateDto.getId() + " should be the same");
        }
        if (!audienceService.isAudienceWithIdExist(id)) {
            throw new EntityNotFoundException("Audience with id: " + id + " is not found");
        }

        audienceService.saveAudience(audience);
        return ResponseEntity.ok(audience);
    }
}
