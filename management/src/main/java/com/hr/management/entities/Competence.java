package com.hr.management.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;  
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "competence")
public class Competence {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_competence")
    private Integer idCompetence;
    private String libelle;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_domaine_competence")
    private CompetenceDomaine domaine;
    // getters / setters

    

    public Competence(Integer idCompetence, String libelle, String description, CompetenceDomaine domaine) {
        this.idCompetence = idCompetence;
        this.libelle = libelle;
        this.description = description;
        this.domaine = domaine;
    }

    public Competence() {
    }

    public Integer getIdCompetence() {
        return idCompetence;
    }

    public void setIdCompetence(Integer idCompetence) {
        this.idCompetence = idCompetence;
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

    public CompetenceDomaine getDomaine() {
        return domaine;
    }

    public void setDomaine(CompetenceDomaine domaine) {
        this.domaine = domaine;
    }
}


