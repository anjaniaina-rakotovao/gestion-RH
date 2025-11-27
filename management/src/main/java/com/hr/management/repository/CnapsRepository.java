package com.hr.management.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.entities.Cnaps;

public interface CnapsRepository extends JpaRepository<Cnaps, Integer>{
    Cnaps findTopByStatusAndDateLessThanEqualOrderByDateDesc(String status, Date date);
}
