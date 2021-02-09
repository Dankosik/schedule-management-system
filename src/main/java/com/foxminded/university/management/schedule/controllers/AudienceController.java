package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AudienceController {
    private final AudienceServiceImpl audienceService;

    public AudienceController(AudienceServiceImpl audienceService) {
        this.audienceService = audienceService;
    }

    @GetMapping("/audiences")
    public String showAllAudiences(Model model) {
        model.addAttribute("audiences", audienceService.getAllAudiences());
        return "audiences";
    }

    @GetMapping("/audiences/{id}")
    public String showOneAudience(@PathVariable("id") Long id, Model model) {
        model.addAttribute("audience", audienceService.getAudienceById(id));
        return "audience";
    }
}
