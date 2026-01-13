package org.example.controllers;

import org.example.dtos.ApiResponse;
import org.example.services.SkillsTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills")
public class SkillTagController {

    @Autowired
    private SkillsTagService skillsTagService;

    @GetMapping
    public ApiResponse allSkills(){
        return new ApiResponse(
                true,
                "All Skills",
                skillsTagService.getAllTags()
        );
    }
}
