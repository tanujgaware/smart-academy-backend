package org.example.controllers;

import org.example.dtos.ApiResponse;
import org.example.dtos.CourseCreateRequest;
import org.example.dtos.EditCoverImg;
import org.example.dtos.VideoRequest;
import org.example.services.courseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private courseService courseService;

    @PostMapping("/")
    public ApiResponse createCourse(@RequestBody CourseCreateRequest request, Authentication authentication){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Course Created",
                courseService.createCourse(request,instructorId)
        );
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ApiResponse myCourses(Authentication authentication){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Fetched My Courses",
                courseService.getMyCourses(instructorId)
        );
    }

    @PutMapping("/{courseId}/publish")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ApiResponse publishCourse(@PathVariable UUID courseId,Authentication authentication){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Course Published",
                courseService.publishCourse(courseId,instructorId)
        );
    }

    @PutMapping("/{courseId}/unpublish")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ApiResponse unpublishCourse(@PathVariable UUID courseId,Authentication authentication){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Course Unpublished",
                courseService.unpublishCourse(courseId,instructorId)
        );
    }

    @PostMapping("/{courseId}/videos")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ApiResponse addVideo(@PathVariable UUID courseId, Authentication authentication, @RequestBody VideoRequest request){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Video Added",
                courseService.addVideo(courseId,instructorId,request)
        );
    }

    @PutMapping("/{courseId}/coverImg")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ApiResponse editCoverImg(@PathVariable UUID courseId, Authentication authentication, @RequestBody EditCoverImg request){
        UUID instructorId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Image Edited",
                courseService.editCoverImg(courseId,instructorId,request)
        );
    }

    @GetMapping("/published")
    public ApiResponse getPublishedCourses(){
        return new ApiResponse(
                true,
                "Published Courses",
                courseService.getPublishedCourses()
        );
    }

    @GetMapping("/{courseId}")
    public ApiResponse getPublishedCourseById(@PathVariable  UUID courseId,Authentication authentication){
        UUID userId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Course Details",
                courseService.getPublishedCourseById(courseId,userId)
        );
    }

    @GetMapping("/{courseId}/cnt")
    public ApiResponse getCourseVid(@PathVariable UUID courseId,Authentication authentication){
        UUID userId=UUID.fromString(authentication.getPrincipal().toString());
        return new ApiResponse(
                true,
                "Content Fetched",
                courseService.getCourseVid(courseId,userId)
        );
    }
}
