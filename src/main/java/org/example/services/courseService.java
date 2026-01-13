package org.example.services;

import org.example.dtos.*;
import org.example.entities.Course;
import org.example.entities.CourseVideo;

import java.util.List;
import java.util.UUID;

public interface courseService {
    Course createCourse(CourseCreateRequest request, UUID instructoruuid);
    CourseResponse publishCourse(UUID courseId, UUID instructorid);
    CourseResponse unpublishCourse(UUID courseId,UUID instructorId);
    List<CourseResponse> getMyCourses(UUID instructorId);
    List<CourseResponse> getPublishedCourses();
    CourseDetailResponse getPublishedCourseById(UUID courseId, UUID userId);
    CourseVideoResponse addVideo(UUID courseId, UUID instructorId , VideoRequest request);
    List<CourseVideoResponse> getCourseVid(UUID courseId,UUID userId);
    Course editCoverImg(UUID courseId, UUID instructorId, EditCoverImg request);

}
