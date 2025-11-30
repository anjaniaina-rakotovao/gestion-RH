package com.hr.management.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.hr.management.entities.EmployeCompetence;
import com.hr.management.entities.Formation;
import com.hr.management.entities.PosteCompetence;
import com.hr.management.repository.CompetenceFormationRepository;
import com.hr.management.repository.EmployeCompetenceRepository;
import com.hr.management.repository.PosteCompetenceRepository;
@Service
public class SuggestionFormationService {

    private final EmployeCompetenceRepository employeCompRepo;
    private final PosteCompetenceRepository posteCompRepo;
    private final CompetenceFormationRepository compFormRepo;

    public SuggestionFormationService(EmployeCompetenceRepository employeCompRepo,
            PosteCompetenceRepository posteCompRepo, CompetenceFormationRepository compFormRepo) {
        this.employeCompRepo = employeCompRepo;
        this.posteCompRepo = posteCompRepo;
        this.compFormRepo = compFormRepo;
    }

    public List<Formation> suggest(String idEmploye, String idPoste) {

    List<PosteCompetence> liste = posteCompRepo.findByIdPoste(idPoste);
    List<Formation> result = new ArrayList<>();

    for (PosteCompetence req : liste) {
        int niveauActuel = employeCompRepo
                .findById_IdEmployeAndId_IdCompetence(idEmploye, req.getIdCompetence())
                .map(EmployeCompetence::getNiveauObtenu)
                .orElse(0);

        if (niveauActuel < req.getNiveauAttendu()) {
            result.addAll(compFormRepo.findFormationsByCompetenceId(req.getIdCompetence()));
        }
    }

    return result;
}

}

