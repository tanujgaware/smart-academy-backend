package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseResponse {
    UUID id;
    String title;
    String description;
    String status;
    Set<String> skills;
    String coverImgUrl;
}
