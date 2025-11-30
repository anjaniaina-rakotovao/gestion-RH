package com.hr.management.entities;

import java.io.Serializable;
import java.util.Objects;

public class PosteCompetenceId implements Serializable {

    private String idPoste;
    private Integer idCompetence;

    public PosteCompetenceId() {}

    public PosteCompetenceId(String idPoste, Integer idCompetence) {
        this.idPoste = idPoste;
        this.idCompetence = idCompetence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PosteCompetenceId)) return false;
        PosteCompetenceId that = (PosteCompetenceId) o;
        return Objects.equals(idPoste, that.idPoste) &&
               Objects.equals(idCompetence, that.idCompetence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPoste, idCompetence);
    }
}
