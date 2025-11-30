package com.hr.management.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "competence_domaine")
public class CompetenceDomaine {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_domaine_competence")
    private Integer idDomaineCompetence;
    private String libelle;
    @Column(columnDefinition = "TEXT")
    private String description;
    public CompetenceDomaine(Integer idDomaineCompetence, String libelle, String description) {
        this.idDomaineCompetence = idDomaineCompetence;
        this.libelle = libelle;
        this.description = description;
    }
    public CompetenceDomaine() {
    }
    // getters / setters
    public Integer getIdDomaineCompetence() {
        return idDomaineCompetence;
    }
    public void setIdDomaineCompetence(Integer idDomaineCompetence) {
        this.idDomaineCompetence = idDomaineCompetence;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
