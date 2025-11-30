package com.hr.management.entities;

import java.io.Serializable;
import java.util.Objects;

public class EmployeCompetenceKey implements Serializable {

    private String idEmploye;
    private Integer idCompetence;

    public EmployeCompetenceKey() {}

    public EmployeCompetenceKey(String idEmploye, Integer idCompetence) {
        this.idEmploye = idEmploye;
        this.idCompetence = idCompetence;
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public Integer getIdCompetence() {
        return idCompetence;
    }

    public void setIdCompetence(Integer idCompetence) {
        this.idCompetence = idCompetence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeCompetenceKey)) return false;
        EmployeCompetenceKey that = (EmployeCompetenceKey) o;
        return Objects.equals(idEmploye, that.idEmploye) &&
               Objects.equals(idCompetence, that.idCompetence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmploye, idCompetence);
    }
}
