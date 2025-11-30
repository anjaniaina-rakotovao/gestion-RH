package com.hr.management.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.hr.management.entities.RapportPerformance;
import com.hr.management.repository.EmployeRepository;
import com.hr.management.repository.RapportPerformanceRepository;

@Service
public class RapportPerformanceService {

    private final ScoringService scoringService;
    private final RapportPerformanceRepository rapportRepo;
    private final EmployeRepository employeRepository;

    public RapportPerformanceService(
            ScoringService scoringService,
            RapportPerformanceRepository rapportRepo,
            EmployeRepository employeRepository) {
        this.scoringService = scoringService;
        this.rapportRepo = rapportRepo;
        this.employeRepository = employeRepository;
    }

    public RapportPerformance genererRapport(String idEmploye, int month, int year, String commentaire) {

        var scoring = scoringService.getScoringPeriodeMonth(month, year);

        RapportPerformance r = new RapportPerformance();
        r.setIdRapport(UUID.randomUUID().toString());
        r.setEmploye(employeRepository.findById(idEmploye).get());
        r.setPeriode(month + "-" + year);
        r.setScoreProductivite(BigDecimal.valueOf(scoring.getScore("Productivité")));
        r.setScoreEfficacite(BigDecimal.valueOf(scoring.getScore("Efficacité")));
        r.setScoreQualite(BigDecimal.valueOf(scoring.getScore("Qualité")));
        r.setCommentaire(commentaire);
        r.setDateGeneration(LocalDate.now());

        return rapportRepo.save(r);
    }
}

