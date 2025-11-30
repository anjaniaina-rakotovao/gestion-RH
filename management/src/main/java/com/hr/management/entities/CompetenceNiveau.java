package com.hr.management.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "competence_niveau")
public class CompetenceNiveau {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNiveau;
    private String code;
    private String libelle;
    private Integer poids;
    public CompetenceNiveau(Integer idNiveau, String code, String libelle, Integer poids) {
        this.idNiveau = idNiveau;
        this.code = code;
        this.libelle = libelle;
        this.poids = poids;
    }
    public CompetenceNiveau() {
    }
    // getters / setters
    public Integer getIdNiveau() {
        return idNiveau;
    }
    public void setIdNiveau(Integer idNiveau) {
        this.idNiveau = idNiveau;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getLibelle() {
        return libelle;
    }
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
    public Integer getPoids() {
        return poids;
    }
    public void setPoids(Integer poids) {
        this.poids = poids;
    }
}
