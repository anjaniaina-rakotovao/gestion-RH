package com.hr.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hr.management.dto.MatchResultDTO;
import com.hr.management.dto.SkillGapDTO;
import com.hr.management.entities.Competence;
import com.hr.management.entities.Employe;
import com.hr.management.entities.EmployeCompetence;
import com.hr.management.entities.PosteCompetence;
import com.hr.management.repository.CompetenceRepository;
import com.hr.management.repository.EmployeCompetenceRepository;
import com.hr.management.repository.PosteCompetenceRepository;

@Service
public class MatchingService {

    private final PosteCompetenceRepository posteCompetenceRepo;
    private final EmployeCompetenceRepository employeCompetenceRepo;
    private final CompetenceRepository competenceRepo;

    public MatchingService(PosteCompetenceRepository pcRepo,
                           EmployeCompetenceRepository ecRepo,
                           CompetenceRepository cRepo) {
        this.posteCompetenceRepo = pcRepo;
        this.employeCompetenceRepo = ecRepo;
        this.competenceRepo = cRepo;
    }

    // Calcul du score et des gaps pour un employé donné et un poste donné
    public MatchResultDTO matchEmployeToPoste(String idEmploye, String idPoste) {
        List<PosteCompetence> reqs = posteCompetenceRepo.findByIdPoste(idPoste);
        List<SkillGapDTO> gaps = new ArrayList<>();
        BigDecimal totalScore = BigDecimal.ZERO;
        int count = 0;

        // Map pour les compétences de l'employé
        Map<Integer, Integer> employeeSkillMap = new HashMap<>();
        for (EmployeCompetence ec : employeCompetenceRepo.findById_IdEmploye(idEmploye)) {
            employeeSkillMap.put(ec.getCompetence().getIdCompetence(), ec.getNiveauObtenu());
        }

        // Calcul du score pour chaque compétence requise
        for (PosteCompetence pc : reqs) {
            int idComp = pc.getIdCompetence();
            int niveauAttendu = pc.getNiveauAttendu();
            Integer niveauActuel = employeeSkillMap.get(idComp);
            BigDecimal scoreComp = BigDecimal.ZERO;

            if (niveauActuel != null) {
                scoreComp = BigDecimal.valueOf(Math.min(1.0, (double) niveauActuel / niveauAttendu));
            }

            totalScore = totalScore.add(scoreComp);
            count++;

            // Ajouter au gap si compétence manquante ou insuffisante
            if (niveauActuel == null || niveauActuel < niveauAttendu) {
                SkillGapDTO gap = new SkillGapDTO();
                gap.setIdCompetence(idComp);
                gap.setCompetenceLibelle(
                    competenceRepo.findById(idComp).map(Competence::getLibelle).orElse("")
                );
                gap.setNiveauAttendu(niveauAttendu);
                gap.setNiveauActuel(niveauActuel);
                gaps.add(gap);
            }
        }

        BigDecimal matchPercent = count == 0 ? BigDecimal.ZERO
                : totalScore.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

        MatchResultDTO res = new MatchResultDTO();
        res.setIdEmploye(idEmploye);
        res.setIdPoste(idPoste);
        res.setMatchScore(matchPercent);
        res.setGaps(gaps);

        return res;
    }

    // Liste des employés triés et filtrés selon le score pour un poste
    public List<Employe> matchPoste(String idPoste) {
        List<EmployeCompetence> allCompetences = employeCompetenceRepo.findAll();

        // Calcul des scores pour chaque employé
        Map<String, BigDecimal> scoreMap = new HashMap<>();
        List<Employe> allEmployes = allCompetences.stream()
                .map(EmployeCompetence::getEmploye)
                .distinct()
                .collect(Collectors.toList());

        for (Employe e : allEmployes) {
            MatchResultDTO result = matchEmployeToPoste(e.getIdEmploye(), idPoste);
            scoreMap.put(e.getIdEmploye(), result.getMatchScore());
        }

        // Filtrer uniquement les employés avec un score > 0 et trier par score décroissant
        List<Employe> sortedList = allEmployes.stream()
                .filter(e -> scoreMap.get(e.getIdEmploye()).compareTo(BigDecimal.ZERO) > 0)
                .sorted((e1, e2) -> scoreMap.get(e2.getIdEmploye())
                                           .compareTo(scoreMap.get(e1.getIdEmploye())))
                .collect(Collectors.toList());

        return sortedList;
    }
}
