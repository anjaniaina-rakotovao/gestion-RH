package com.hr.management.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.hr.management.entities.Employe;
import com.hr.management.entities.Evaluation;   
import com.hr.management.repository.EvaluationRepository;

@Service
public class EvaluationService {

    private final EvaluationRepository repo;

    public EvaluationService(EvaluationRepository repo) {
        this.repo = repo;
    }

    public Evaluation genererEvaluation(Employe emp, String periode, 
                                        BigDecimal prod, BigDecimal eff, BigDecimal qual) {

        BigDecimal scoreGlobal = prod.add(eff).add(qual)
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

        Evaluation e = new Evaluation();
        e.setEmploye(emp);
        e.setPeriode(periode);
        e.setProductivite(prod);
        e.setEfficacite(eff);
        e.setQualite(qual);
        e.setScoreGlobal(scoreGlobal);
        e.setDateEvaluation(LocalDate.now());

        return repo.save(e);
    }
}

