package com.hr.management.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.entities.Formation;
public interface FormationRepository extends JpaRepository<Formation, Integer> {}
