package com.hr.management.controller;

import org.springframework.web.bind.annotation.*;

import com.hr.management.entities.Utilisateur;
import com.hr.management.service.UtilisateurService;

@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    private final UtilisateurService service;

    public UtilisateurController(UtilisateurService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public Utilisateur login(@RequestParam String login, @RequestParam String password) {
        return service.authenticate(login, password);
    }

    @GetMapping
    public java.util.List<Utilisateur> all() { return service.getAll(); }
}
