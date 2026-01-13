package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class SkillTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable = false)
    private String name;

}
