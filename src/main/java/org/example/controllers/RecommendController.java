package org.example.controllers;

import org.example.dtos.ApiResponse;
import org.example.services.recommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recommend")
public class RecommendController {
    @Autowired
    private recommendService recommendService;

    @GetMapping("/v1")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse recommendCourse(Authentication authentication){
        UUID userId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Recommendation Fetched SuccessFully",
                recommendService.recommendv1(userId)
        );
    }


}
