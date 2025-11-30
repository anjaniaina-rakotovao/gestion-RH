// Dans com.hr.management.service.EmployeCompetenceService
package com.hr.management.service;

import com.hr.management.entities.EmployeCompetence;
import com.hr.management.repository.EmployeCompetenceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeCompetenceService {

    private final EmployeCompetenceRepository employeCompRepo;

    public EmployeCompetenceService(EmployeCompetenceRepository employeCompRepo) {
        this.employeCompRepo = employeCompRepo;
    }


    public List<EmployeCompetence> getCompetencesByEmploye(String idEmploye) {

        return employeCompRepo.findById_IdEmploye(idEmploye);
    }
}