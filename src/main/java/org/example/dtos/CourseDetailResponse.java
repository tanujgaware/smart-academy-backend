package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseDetailResponse {
    UUID id;
    String title;
    String description;
    String status;
    Set<String> skills;
    String coverImgUrl;
    Boolean enrollment;
    Boolean isInstructor;
}
