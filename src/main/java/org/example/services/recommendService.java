package org.example.services;

import org.example.dtos.CourseResponse;

import java.util.List;
import java.util.UUID;

public interface recommendService {
    List<CourseResponse>recommendv1(UUID userId);
//    List<CourseResponse>recommendv2(UUID userId);
}
