package com.hr.management.entities;

import java.io.Serializable;
import java.util.Objects;

public class FormationCompetenceId implements Serializable {

    private Integer idFormation;
    private Integer idCompetence;

    public FormationCompetenceId() {}

    public FormationCompetenceId(Integer idFormation, Integer idCompetence) {
        this.idFormation = idFormation;
        this.idCompetence = idCompetence;
    }

    public Integer getIdFormation() { return idFormation; }
    public void setIdFormation(Integer idFormation) { this.idFormation = idFormation; }

    public Integer getIdCompetence() { return idCompetence; }
    public void setIdCompetence(Integer idCompetence) { this.idCompetence = idCompetence; }
    @Override
    public int hashCode() {
        return Objects.hash(idFormation, idCompetence);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FormationCompetenceId)) return false;
        FormationCompetenceId other = (FormationCompetenceId) obj;
        return Objects.equals(idFormation, other.idFormation)
            && Objects.equals(idCompetence, other.idCompetence);
    }
}


