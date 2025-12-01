package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.Utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    Optional<Utilisateur> findByLogin(String login);
}
