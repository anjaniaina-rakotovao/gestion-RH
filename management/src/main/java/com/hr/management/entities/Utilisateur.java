package com.hr.management.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @Column(name = "id_utilisateur")
    private String idUtilisateur;

    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @Column(unique = true)
    private String login;

    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private Boolean actif = true;

    // Getters / Setters
    public String getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(String idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}
