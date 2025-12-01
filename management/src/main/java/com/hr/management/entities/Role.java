package com.hr.management.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "id_role")
    private String idRole;

    private String libelle;

    @OneToMany(mappedBy = "role")
    private List<Utilisateur> utilisateurs;

    // Getters / Setters
    public String getIdRole() { return idRole; }
    public void setIdRole(String idRole) { this.idRole = idRole; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public List<Utilisateur> getUtilisateurs() { return utilisateurs; }
    public void setUtilisateurs(List<Utilisateur> utilisateurs) { this.utilisateurs = utilisateurs; }
}
