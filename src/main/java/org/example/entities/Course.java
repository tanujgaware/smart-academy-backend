package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.*;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length=2000)
    private String description;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "instructor_id",nullable = false)
    private User instructor;

    @Enumerated(EnumType.STRING)
    private CourseStatus status=CourseStatus.DRAFT;

    @ManyToMany
    @JoinTable(
            name="course_skill_tags",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns = @JoinColumn(name="skill_tag_id")
    )
    private Set<SkillTag> skillsTags=new HashSet<>();

    @OneToMany(mappedBy = "course",cascade=CascadeType.ALL,orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<CourseVideo> videos=new ArrayList<>();

    @Column(name="coverImg")
    private String coverImgUrl;
}

