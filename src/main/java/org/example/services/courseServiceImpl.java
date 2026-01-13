package org.example.services;

import com.mysql.cj.x.protobuf.MysqlxSession;
import org.example.dtos.*;
import org.example.entities.*;
import org.example.exceptions.BadRequestException;
import org.example.repositories.CourseRepository;
import org.example.repositories.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class courseServiceImpl implements courseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SkillsTagService skillsTagService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

//    @Override
//    public Optional<Course> findById(String id){
//        return courseRepository.findById(id);
//    }

    @Override
    public Course createCourse(CourseCreateRequest request, UUID instructoruuid){
        User instructor=userService.findById(instructoruuid);
        Course course=new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructor(instructor);
        course.setStatus(CourseStatus.DRAFT);
        Set<SkillTag> tags=skillsTagService.getOrCreateTags(request.getSkills());
        course.setSkillsTags(tags);
        course.setCoverImgUrl(request.getUrl());
        return courseRepository.save(course);
    }

    @Override
    public List<CourseResponse> getMyCourses(UUID instructorId){
        User instructor=userService.findById(instructorId);
        return courseRepository.findByInstructor(instructor).stream()
                .map(course->new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getStatus().name(),
                        course.getSkillsTags()
                                .stream()
                                .map(SkillTag::getName)
                                .collect(Collectors.toSet()),
                        course.getCoverImgUrl()
                )).toList();
    }

    @Override
    public List<CourseResponse> getPublishedCourses(){
        return courseRepository.findByStatus(CourseStatus.PUBLISHED).stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getStatus().name(),
                        course.getSkillsTags()
                                .stream()
                                .map(SkillTag::getName)
                                .collect(Collectors.toSet()),
                        course.getCoverImgUrl()
                ))
                .toList();
    }

    @Override
    public CourseDetailResponse getPublishedCourseById(UUID courseId,UUID userId){
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("Course Not Found"));
        User user=userService.findById(userId);
        boolean enrollment =enrollmentRepository.existsByUserAndCourse(user,course);
        boolean isInstructor=course.getInstructor().getId()==user.getId();
        if(isInstructor){
            return new CourseDetailResponse(
                    course.getId(),
                    course.getTitle(),
                    course.getDescription(),
                    course.getStatus().name(),
                    course.getSkillsTags()
                            .stream()
                            .map(SkillTag::getName)
                            .collect(Collectors.toSet()),
                    course.getCoverImgUrl(),
                    enrollment,
                    isInstructor
            );
        }
        if(course.getStatus()!=CourseStatus.PUBLISHED){
            throw new BadRequestException("Course Not Accessible");
        }
        return new CourseDetailResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getStatus().name(),
                course.getSkillsTags()
                        .stream()
                        .map(SkillTag::getName)
                        .collect(Collectors.toSet()),
                course.getCoverImgUrl(),
                enrollment,
                isInstructor
        );
    }

    @Override
    public CourseResponse publishCourse(UUID courseId,UUID instructorid){
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("No such Course"));
        UUID userId=course.getInstructor().getId();
        if(!userId.equals(instructorid)){
            throw new BadRequestException("Not Authorized");
        }
        course.setStatus(CourseStatus.PUBLISHED);
        courseRepository.save(course);
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getStatus().name(),
                course.getSkillsTags()
                        .stream()
                        .map(SkillTag::getName)
                        .collect(Collectors.toSet()),
                course.getCoverImgUrl()
        );
    }

    @Override
    public CourseResponse unpublishCourse(UUID courseId, UUID instructorId){
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("No such Course"));
        UUID userId=course.getInstructor().getId();
        if(!userId.equals(instructorId)){
            throw new BadRequestException("You are not allowed to access these course");
        }
        course.setStatus(CourseStatus.DRAFT);
        courseRepository.save(course);
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getStatus().name(),
                course.getSkillsTags()
                        .stream()
                        .map(SkillTag::getName)
                        .collect(Collectors.toSet()),
                course.getCoverImgUrl()
        );
    }

    @Override
    public CourseVideoResponse addVideo(UUID courseId, UUID instructorId, VideoRequest request) {
        Course course =courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("Course Not Found"));
        if(!course.getInstructor().getId().equals(instructorId)){
            throw new BadRequestException("Not Authorized");
        }
        CourseVideo courseVideo=new CourseVideo();
        courseVideo.setCourse(course);
        courseVideo.setTitle(request.getTitle());
        courseVideo.setVideoUrl(request.getVideoUrl());
        courseVideo.setOrderIndex(course.getVideos().size()+1);
        course.getVideos().add(courseVideo);

        courseRepository.saveAndFlush(course);
        return new CourseVideoResponse(
                courseVideo.getId(),
                courseVideo.getTitle(),
                courseVideo.getVideoUrl(),
                courseVideo.getOrderIndex()
        );
    }

    @Override
    public List<CourseVideoResponse> getCourseVid(UUID courseId,UUID userId){
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("Course Not found"));

        boolean isInstructor=course.getInstructor().getId().equals(userId);
        if(course.getStatus()!=CourseStatus.PUBLISHED && !isInstructor){
            throw new BadRequestException("Course Not Accessible");
        }

        User user=userService.findById(userId);
        boolean isenrolled=enrollmentRepository.existsByUserAndCourse(user,course);
        if(!isInstructor && !isenrolled){
            throw new BadRequestException("You are not allowed to access these course");
        }
        return course.getVideos().stream()
                .map(ele->new CourseVideoResponse(
                        ele.getId(),
                        ele.getTitle(),
                        ele.getVideoUrl(),
                        ele.getOrderIndex()
                )).toList();
    }

    @Override
    public Course editCoverImg(UUID courseId, UUID instructorId, EditCoverImg request){
        Course course=courseRepository.findById(courseId)
                .orElseThrow(()->new BadRequestException("Course Not Found"));
        if(!course.getInstructor().getId().equals(instructorId)){
            throw new BadRequestException("Not Authorized");

        }
        course.setCoverImgUrl(request.getUrl());
        courseRepository.save(course);
        return course;
    }
}

