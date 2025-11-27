package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hr.management.entities.Cnaps;
import java.util.Date;

public interface CnapsRepository extends JpaRepository<Cnaps, Integer>{
    Cnaps findTopByStatusAndDateLessThanEqualOrderByDateDesc(String status, Date date);
}
