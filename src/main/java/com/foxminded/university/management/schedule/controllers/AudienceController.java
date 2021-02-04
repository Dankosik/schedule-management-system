package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.AudienceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AudienceController {
    private final AudienceService audienceService;

    public AudienceController(AudienceService audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping("/audiences")
    public String showAllAudiences(Model model) {
        model.addAttribute("audiences", audienceService.getAllAudiences());
        model.addAttribute("newAudience", new Audience(0, 0, 1L));
        return "audiences";
    }

    @GetMapping("/audiences/{id}")
    public String showOneAudience(@PathVariable("id") Long id, Model model) {
        model.addAttribute("audience", audienceService.getAudienceById(id));
        return "audience";
    }
}
