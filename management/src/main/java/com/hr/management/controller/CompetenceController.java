package com.hr.management.controller;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr.management.entities.Competence;
import com.hr.management.entities.CompetenceDomaine;
import com.hr.management.entities.Employe;
import com.hr.management.entities.EmployeCompetence;
import com.hr.management.entities.Formation;
import com.hr.management.entities.PosteCompetence;
import com.hr.management.service.MatchingService;
import com.hr.management.service.SuggestionFormationService;
import com.hr.management.service.CompetenceService;
import com.hr.management.service.CompetenceDomaineService;
import com.hr.management.service.EmployeCompetenceService;
import com.hr.management.service.PosteCompetenceService;


@RestController
@RequestMapping("/api/competence")
public class CompetenceController {

    private final MatchingService matchingService;
    private final SuggestionFormationService formationService;
    private final CompetenceService competenceService;
    private final CompetenceDomaineService competenceDomaineService;
    private final EmployeCompetenceService employeCompetenceService;
    private final PosteCompetenceService posteCompetenceService;


    public CompetenceController(MatchingService matchingService, SuggestionFormationService formationService, CompetenceService competenceService, CompetenceDomaineService competenceDomaineService, EmployeCompetenceService employeCompetenceService, PosteCompetenceService posteCompetenceService) {
        this.matchingService = matchingService;
        this.formationService = formationService;
        this.competenceService = competenceService;
        this.competenceDomaineService = competenceDomaineService;
        this.employeCompetenceService = employeCompetenceService;
        this.posteCompetenceService = posteCompetenceService;
    }

    @GetMapping("/matching/{idPoste}")
    public List<Employe> match(@PathVariable String idPoste) {
        return matchingService.matchPoste(idPoste);
    }

    @GetMapping("/suggest/{idEmploye}/{idPoste}")
    public List<Formation> suggest(@PathVariable String idEmploye, @PathVariable String idPoste) {
        return formationService.suggest(idEmploye, idPoste);
    }

    @GetMapping("/all")
    public List<Competence> getAllCompetences() {
        return competenceService.findAll();
    }
    
    @GetMapping("/domaines")
    public List<CompetenceDomaine> getAllDomaines() {
        return competenceDomaineService.findAll();
    }

    @GetMapping("/employe/{idEmploye}")
    public List<EmployeCompetence> getEmployeCompetences(@PathVariable String idEmploye) {
        return employeCompetenceService.getCompetencesByEmploye(idEmploye);
    }
    
    @GetMapping("/poste/{idPoste}")
    public List<PosteCompetence> getPosteCompetences(@PathVariable String idPoste) {
        // Supposons que ce service utilise le PosteCompetenceRepository
        return posteCompetenceService.getRequiredCompetences(idPoste);
    }
}

