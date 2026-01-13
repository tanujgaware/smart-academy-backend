package org.example.repositories;

import org.example.entities.SkillTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillTagRepository extends JpaRepository<SkillTag,Long> {
    Optional<SkillTag> findByName(String name);
}
