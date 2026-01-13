package org.example.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class CourseCreateRequest {
    private String title;
    private String description;
    private Set<String> skills;
    private String url;
}
