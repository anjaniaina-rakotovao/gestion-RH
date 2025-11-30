// Dans com.hr.management.repository.CompetenceDomaineRepository
package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.CompetenceDomaine;

public interface CompetenceDomaineRepository extends JpaRepository<CompetenceDomaine, Integer> {
    // Cette interface doit rester vide, Spring Data JPA fait le reste.
}