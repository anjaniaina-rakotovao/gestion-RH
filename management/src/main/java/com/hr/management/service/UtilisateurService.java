package com.hr.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.entities.Utilisateur;
import com.hr.management.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository repo;

    public UtilisateurService(UtilisateurRepository repo) {
        this.repo = repo;
    }

    public Utilisateur authenticate(String login, String password) {

        return repo.findByLogin(login)
                   .filter(u -> u.getMotDePasse().equals(password))
                   .orElse(null);
    }

    public List<Utilisateur> getAll() {
        return repo.findAll();
    }
}
