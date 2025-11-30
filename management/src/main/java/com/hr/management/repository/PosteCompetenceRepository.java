package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.PosteCompetence;
import com.hr.management.entities.PosteCompetenceId;
import java.util.List;
public interface PosteCompetenceRepository extends JpaRepository<PosteCompetence, PosteCompetenceId> {
    List<PosteCompetence> findByIdPoste(String idPoste);
    
}
