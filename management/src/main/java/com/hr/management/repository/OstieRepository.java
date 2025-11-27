package com.hr.management.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.entities.Ostie;

public interface OstieRepository extends JpaRepository<Ostie, Integer>{
    Ostie findTopByStatusAndDateLessThanEqualOrderByDateDesc(String status, Date date);
}
