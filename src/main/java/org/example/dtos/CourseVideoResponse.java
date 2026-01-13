package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseVideoResponse {
    UUID id;
    String title;
    String videoUrl;
    int orderIndex;
}
