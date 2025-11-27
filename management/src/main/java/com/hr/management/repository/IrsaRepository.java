package com.hr.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr.management.entities.Irsa;

public interface IrsaRepository extends JpaRepository<Irsa, Integer>{
    Irsa findByTauxAndDate(double taux, Date date);
    List<Irsa> findByDateLessThanEqualOrderByMontantMinAsc(Date date);

    @Query("SELECT i FROM Irsa i WHERE i.taux = :taux AND i.date <= :date ORDER BY i.date DESC")
    List<Irsa> findLatestByTaux(@Param("taux") double taux, @Param("date") Date date);
}

