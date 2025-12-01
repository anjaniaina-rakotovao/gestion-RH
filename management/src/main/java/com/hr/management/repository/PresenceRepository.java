package com.hr.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr.management.entities.Employe;
import com.hr.management.entities.Presence;

public interface PresenceRepository extends JpaRepository<Presence, String> {

    // Toutes les présences d'un employé
    List<Presence> findByEmployeIdEmploye(String idEmploye);

    // Toutes les présences d'un employé pour une date précise
    List<Presence> findByEmployeIdEmployeAndDate(String idEmploye, Date date);

    // Récupérer tous les mouvements d’un employé pour une date donnée
    List<Presence> findByEmployeAndDateBetween(Employe employe, Date start, Date end);

    List<Presence> findByEmployeIdEmployeAndDateBetween(String idEmploye, Date start, Date end);

    // Récupérer toutes les présences pour une date
    List<Presence> findByDateBetween(Date start, Date end);

    @Query("SELECT p FROM Presence p WHERE p.employe.idEmploye = :id AND p.date >= :start AND p.date < :end")
    List<Presence> findByEmployeIdAndDate(
            @Param("id") String id,
            @Param("start") Date start,
            @Param("end") Date end);


}
