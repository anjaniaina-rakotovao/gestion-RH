package com.hr.management.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr.management.entities.HistoMouvement;

public interface HistoMouvementRepository extends JpaRepository<HistoMouvement, String> {

        @Query("SELECT SUM(h.absence) FROM HistoMouvement h " +
                        "WHERE h.employe.idEmploye = :idEmp " +
                        "AND h.date BETWEEN :debutMois AND :finMois")
        Integer findAbsenceByEmployeAndMonth(
                        @Param("idEmp") String idEmp,
                        @Param("debutMois") LocalDate debutMois,
                        @Param("finMois") LocalDate finMois);

        @Query("SELECT e.poste.idPoste FROM EmployePoste e " +
                        "WHERE e.employe.idEmploye = :idEmp " +
                        "AND e.dateDebut <= :finMois " +
                        "AND (e.dateFin IS NULL OR e.dateFin >= :debutMois)")
        String findIdPosteByEmploye(
                        @Param("idEmp") String idEmp,
                        @Param("debutMois") LocalDate debutMois,
                        @Param("finMois") LocalDate finMois);

        List<HistoMouvement> findByEmployeIdEmploye(String idEmploye);

        // Ligne pour un employé et une date spécifique
        Optional<HistoMouvement> findByEmployeIdEmployeAndDate(String idEmploye, Date date);

        @Query("SELECT h FROM HistoMouvement h WHERE h.employe.idEmploye = :id AND MONTH(h.date) = :month AND YEAR(h.date) = :year")
        List<HistoMouvement> findByEmployeAndMonthAndYear(
                        @Param("id") String id,
                        @Param("month") int month,
                        @Param("year") int year);

        List<HistoMouvement> findByDateBetween(LocalDate start, LocalDate end);

        // Récupérer tous les mouvements pour une année et un mois
        @Query("SELECT h FROM HistoMouvement h WHERE YEAR(h.date) = :year AND MONTH(h.date) = :month")
        List<HistoMouvement> findByYearAndMonth(@Param("year") int year, @Param("month") int month);

        @Query("SELECT h FROM HistoMouvement h WHERE h.employe.idEmploye IN :employeIds AND YEAR(h.date) = :year")
        List<HistoMouvement> findByYearAndEmployes(@Param("year") int year,
                        @Param("employeIds") List<String> employeIds);

        @Query("SELECT h FROM HistoMouvement h WHERE h.employe.idEmploye IN :employeIds AND YEAR(h.date) = :year AND MONTH(h.date) = :month")
        List<HistoMouvement> findByYearMonthAndEmployes(@Param("year") int year, @Param("month") int month,
                        @Param("employeIds") List<String> employeIds);

        @Query("SELECT h FROM HistoMouvement h WHERE YEAR(h.date) = :year")
        List<HistoMouvement> findByYear(@Param("year") int year);
}
