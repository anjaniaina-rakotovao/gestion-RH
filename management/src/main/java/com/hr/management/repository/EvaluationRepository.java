package com.hr.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.entities.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

    // Ici on remonte correctement à Employe.idEmploye
    List<Evaluation> findByEmployeIdEmployeAndPeriode(String idEmploye, String periode);

    List<Evaluation> findByPeriode(String periode);
}
