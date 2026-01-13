package org.example.services;

import org.example.entities.SkillTag;

import java.util.List;
import java.util.Set;

public interface SkillsTagService {
    Set<SkillTag> getOrCreateTags(Set<String> skillName);

    List<SkillTag> getAllTags();
}