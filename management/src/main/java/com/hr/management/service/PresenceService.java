package com.hr.management.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.entities.Employe;
import com.hr.management.entities.Presence;
import com.hr.management.repository.PresenceRepository;

@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;

    public PresenceService(PresenceRepository presenceRepository) {
        this.presenceRepository = presenceRepository;
    }

    public List<Presence> getMovementsForDay(Date start, Date end) {
        return presenceRepository.findByDateBetween(start, end);
    }

    public List<Presence> getMovementsForEmployee(Employe emp, Date start, Date end) {
        return presenceRepository.findByEmployeAndDateBetween(emp, start, end);
    }

    public Presence save(Presence p) {
        return presenceRepository.save(p);
    }

}
