package com.hr.management.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr.management.entities.Competence;

public interface CompetenceRepository extends JpaRepository<Competence, Integer> {

    @Query("SELECT c FROM Competence c WHERE c.domaine.id = :domaineId")
    List<Competence> findCompetencesByDomaineId(@Param("domaineId") Integer domaineId);
}

