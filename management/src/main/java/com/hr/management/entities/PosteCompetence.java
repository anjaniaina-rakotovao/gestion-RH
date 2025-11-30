package com.hr.management.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "poste_competence")
@IdClass(PosteCompetenceId.class)
public class PosteCompetence {

    @Id
    @Column(name = "id_poste")
    private String idPoste;

    @Id
    @Column(name = "id_competence")
    private Integer idCompetence;

    @Column(name = "niveau_attendu")
    private Integer niveauAttendu;

    public PosteCompetence() {}

    public String getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }

    public Integer getIdCompetence() {
        return idCompetence;
    }

    public void setIdCompetence(Integer idCompetence) {
        this.idCompetence = idCompetence;
    }

    public Integer getNiveauAttendu() {
        return niveauAttendu;
    }

    public void setNiveauAttendu(Integer niveauAttendu) {
        this.niveauAttendu = niveauAttendu;
    }
}
