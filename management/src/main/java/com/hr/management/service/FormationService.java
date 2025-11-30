package com.hr.management.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.hr.management.dto.FormationSuggestionDTO;
import com.hr.management.dto.SkillGapDTO;
import com.hr.management.repository.CompetenceRepository;
import com.hr.management.repository.FormationCompetenceRepository;
import com.hr.management.repository.FormationRepository;


@Service
public class FormationService {
    private final FormationRepository formationRepo;
    private final FormationCompetenceRepository formationCompetenceRepo;
    private final CompetenceRepository competenceRepo;

    public FormationService(FormationRepository fr, FormationCompetenceRepository fcr, CompetenceRepository cr) {
        this.formationRepo = fr;
        this.formationCompetenceRepo = fcr;
        this.competenceRepo = cr;
    }   
    // inject formationRepo, formationCompetenceRepo, competenceRepo
    public List<FormationSuggestionDTO> suggestFormationsForGaps(List<SkillGapDTO> gaps) {
        Set<Integer> gapCompIds = gaps.stream().map(SkillGapDTO::getIdCompetence).collect(Collectors.toSet());
        // recherche formations qui couvrent au moins une des gapCompIds
        List<FormationSuggestionDTO> suggestions = new ArrayList<>();
        // simple query + aggregation -> build DTOs
        return suggestions;
    }
}

