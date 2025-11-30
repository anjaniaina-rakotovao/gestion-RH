package com.hr.management.repository;

import java.util.List;

import com.hr.management.entities.TicketHisto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketHistoRepository extends JpaRepository<TicketHisto, String> {

    @Query("SELECT th FROM TicketHisto th JOIN FETCH th.ticket t " +
              "WHERE FUNCTION('YEAR', th.dateStatut) = :year " +
              "AND FUNCTION('MONTH', th.dateStatut) = :month")
    List<TicketHisto> getHistoByMonthYear(@Param("month") int month, @Param("year") int year);

       @Query("SELECT th FROM TicketHisto th JOIN FETCH th.ticket t " +
              "WHERE FUNCTION('YEAR', th.dateStatut) = :year")
       List<TicketHisto> getHistoByYear(@Param("year") int year);
}
