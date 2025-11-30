package com.hr.management.entities;

import java.time.LocalDate;
import java.math.BigDecimal;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class RapportPerformance {

    @Id
    private String idRapport;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    private String periode;

    private BigDecimal scoreProductivite;
    private BigDecimal scoreEfficacite;
    private BigDecimal scoreQualite;

    private String commentaire;

    private LocalDate dateGeneration;

    public RapportPerformance(String idRapport, Employe employe, String periode, BigDecimal scoreProductivite,
            BigDecimal scoreEfficacite, BigDecimal scoreQualite, String commentaire, LocalDate dateGeneration) {
        this.idRapport = idRapport;
        this.employe = employe;
        this.periode = periode;
        this.scoreProductivite = scoreProductivite;
        this.scoreEfficacite = scoreEfficacite;
        this.scoreQualite = scoreQualite;
        this.commentaire = commentaire;
        this.dateGeneration = dateGeneration;
    }

    public RapportPerformance() {
    }

    public String getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(String idRapport) {
        this.idRapport = idRapport;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public BigDecimal getScoreProductivite() {
        return scoreProductivite;
    }

    public void setScoreProductivite(BigDecimal scoreProductivite) {
        this.scoreProductivite = scoreProductivite;
    }

    public BigDecimal getScoreEfficacite() {
        return scoreEfficacite;
    }

    public void setScoreEfficacite(BigDecimal scoreEfficacite) {
        this.scoreEfficacite = scoreEfficacite;
    }

    public BigDecimal getScoreQualite() {
        return scoreQualite;
    }

    public void setScoreQualite(BigDecimal scoreQualite) {
        this.scoreQualite = scoreQualite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDate getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDate dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    // getters + setters
}

