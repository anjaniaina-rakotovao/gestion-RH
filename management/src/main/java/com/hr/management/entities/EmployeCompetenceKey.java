package com.hr.management.entities;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
    
@Embeddable
public class EmployeCompetenceKey implements Serializable {
    private String idEmploye;

    private Integer idCompetence;

    // co
    // nstructors, equals, hashCode

    public EmployeCompetenceKey() {
    }   
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
        
}

