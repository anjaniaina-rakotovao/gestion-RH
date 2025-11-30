package com.hr.management.dto;

import java.math.BigDecimal;

public class ScoringTicketDTO {

    private String nom;
    private BigDecimal score;

    public ScoringTicketDTO() {
    }

    public ScoringTicketDTO(String nom, BigDecimal score) {
        this.nom = nom;
        this.score = score;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
