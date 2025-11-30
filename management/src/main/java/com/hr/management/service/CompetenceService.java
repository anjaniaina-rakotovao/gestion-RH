package com.hr.management.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.entities.Competence;
import com.hr.management.entities.Employe;
import com.hr.management.entities.EmployeCompetence;
import com.hr.management.entities.EmployeCompetenceKey;
import com.hr.management.repository.CompetenceRepository;
import com.hr.management.repository.EmployeCompetenceRepository;

    @Service
public class CompetenceService {

    private final CompetenceRepository competenceRepo;
    private final EmployeCompetenceRepository employeCompetenceRepo;

    public CompetenceService(CompetenceRepository c, EmployeCompetenceRepository ec) {
        this.competenceRepo = c;
        this.employeCompetenceRepo = ec;
    }

    public Competence ajouterCompetence(String libelle, String description) {
        Competence c = new Competence();
        c.setLibelle(libelle);
        c.setDescription(description);
        return competenceRepo.save(c);
    }

    public EmployeCompetence ajouterCompetenceEmploye(Employe e, Competence c, int niveau) {
        EmployeCompetence ec = new EmployeCompetence();
        ec.setId(new EmployeCompetenceKey(e.getIdEmploye(), c.getIdCompetence()));
        ec.setEmploye(e);
        ec.setCompetence(c);
        ec.setNiveauObtenu(niveau);
        ec.setDateAttribution(LocalDate.now());
        return employeCompetenceRepo.save(ec);
    }

    public List<Competence> findAll() {
        return competenceRepo.findAll();
    }
    

}

