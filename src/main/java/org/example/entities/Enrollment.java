package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id,course_id"})
)
public class Enrollment {
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="student_id",nullable = false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="course_id",nullable = false)
    private Course course;
}
