package com.hr.management.entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "formation_competence")
@IdClass(FormationCompetenceId.class)
public class FormationCompetence {
    @Id
    @Column(name = "id_formation")
    private Integer idFormation;
    @Id
    @Column(name = "id_competence")
    private Integer idCompetence;

    private Integer poids;

    @ManyToOne // Relation vers la table Formation
    @JoinColumn(name = "id_formation", insertable = false, updatable = false)
    private Formation formation;
    public FormationCompetence(Integer idFormation, Integer idCompetence) {
        this.idFormation = idFormation;
        this.idCompetence = idCompetence;
    }
    public FormationCompetence() {
    }
    // Optionnel : niveau visé, etc.
    public Integer getIdFormation() {
        return idFormation;
    }
    public void setIdFormation(Integer idFormation) {
        this.idFormation = idFormation;
    }
    public Integer getIdCompetence() {
        return idCompetence;
    }
    public void setIdCompetence(Integer idCompetence) {
        this.idCompetence = idCompetence;
    }

    public Integer getPoids() { return poids; }
public void setPoids(Integer poids) { this.poids = poids; }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }
}
