package com.hr.management.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_histo")
public class TicketHisto {

   @Id
    @Column(name = "id_ticket_histo")
    private String idTicketHisto;

    @ManyToOne
    @JoinColumn(name = "id_ticket")
    @JsonManagedReference(value = "histo-tickets")
    private Ticket ticket;

    @Column(name = "date_statut")
    private LocalDate dateStatut;

    @Column(name = "duree")
    private BigDecimal duree;

    @Column(name = "statut")
    private int statut;

    public TicketHisto() {
    }

    public TicketHisto(String idTicketHisto, Ticket ticket, LocalDate dateStatut, BigDecimal duree, int statut) {
        this.idTicketHisto = idTicketHisto;
        this.ticket = ticket;
        this.dateStatut = dateStatut;
        this.duree = duree;
        this.statut = statut;
    }

    public String getIdTicketHisto() {
        return idTicketHisto;
    }

    public void setIdTicketHisto(String idTicketHisto) {
        this.idTicketHisto = idTicketHisto;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public LocalDate getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(LocalDate dateStatut) {
        this.dateStatut = dateStatut;
    }

    public BigDecimal getDuree() {
        return duree;
    }

    public void setDuree(BigDecimal duree) {
        this.duree = duree;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

}

