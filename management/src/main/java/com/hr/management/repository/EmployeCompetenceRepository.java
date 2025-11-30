package com.hr.management.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.EmployeCompetence;
import com.hr.management.entities.EmployeCompetenceKey;

public interface EmployeCompetenceRepository extends JpaRepository<EmployeCompetence, EmployeCompetenceKey> {

    // Pour récupérer une compétence précise d’un employé
    Optional<EmployeCompetence> findById_IdEmployeAndId_IdCompetence(String idEmploye, Integer idCompetence);

    // Pour récupérer toutes les compétences d’un employé
    List<EmployeCompetence> findById_IdEmploye(String idEmploye);
}
