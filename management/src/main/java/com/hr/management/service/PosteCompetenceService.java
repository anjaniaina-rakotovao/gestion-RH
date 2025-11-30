// Dans com.hr.management.service.PosteCompetenceService
package com.hr.management.service;

import com.hr.management.entities.PosteCompetence;
import com.hr.management.repository.PosteCompetenceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PosteCompetenceService {

    private final PosteCompetenceRepository posteCompRepo;

    public PosteCompetenceService(PosteCompetenceRepository posteCompRepo) {
        this.posteCompRepo = posteCompRepo;
    }


    public List<PosteCompetence> getRequiredCompetences(String idPoste) {

        return posteCompRepo.findByIdPoste(idPoste);
    }
}