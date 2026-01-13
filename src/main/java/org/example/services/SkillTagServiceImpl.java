package org.example.services;

import org.example.entities.SkillTag;
import org.example.repositories.SkillTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SkillTagServiceImpl implements SkillsTagService{
    @Autowired
    private SkillTagRepository skillTagRepository;

    @Override
    public Set<SkillTag> getOrCreateTags(Set<String> skillNames){
        Set<SkillTag>tags=new HashSet<>();
        for(String rawName:skillNames){
            String normalized=rawName.trim().toLowerCase();

            SkillTag tag=skillTagRepository
                    .findByName(normalized)
                    .orElseGet(()->{
                        SkillTag newTag=new SkillTag();
                        newTag.setName(normalized);
                        return skillTagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public List<SkillTag>getAllTags(){
        return skillTagRepository.findAll();
    }
}
