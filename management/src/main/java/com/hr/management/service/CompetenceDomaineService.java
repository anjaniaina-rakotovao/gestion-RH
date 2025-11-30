
package com.hr.management.service;

import com.hr.management.entities.CompetenceDomaine;
import com.hr.management.repository.CompetenceDomaineRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompetenceDomaineService {

    private final CompetenceDomaineRepository domaineRepo;

    public CompetenceDomaineService(CompetenceDomaineRepository domaineRepo) {
        this.domaineRepo = domaineRepo;
    }

    public List<CompetenceDomaine> findAll() {
        return domaineRepo.findAll(); 
    }
}