package com.hr.management.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hr.management.entities.Employe;
import com.hr.management.entities.HistoMouvement;
import com.hr.management.entities.Presence;
import com.hr.management.repository.EmployeRepository;
import com.hr.management.repository.HistoMouvementRepository;
import com.hr.management.repository.PresenceRepository;

@Service
public class HistoMouvementService {

        @Autowired
        private PresenceRepository presenceRepository;

        @Autowired
        private EmployeRepository employeRepository;

        @Autowired
        private HistoMouvementRepository histoRepo;

        @Transactional
        public void closeJourney(LocalDate dateToClose) {

                // Récupérer tous les employés
                List<Employe> allEmployees = employeRepository.findAll();

                // Définir période du jour
                Date start = Date.from(dateToClose.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(dateToClose.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

                for (Employe emp : allEmployees) {

                        // Récupérer les présences du jour pour cet employé
                        List<Presence> mouvements = presenceRepository
                                        .findByEmployeIdEmployeAndDateBetween(emp.getIdEmploye(), start, end);
                        ;

                        Integer retard = null;
                        Integer heureSup = null;
                        Integer pauses = null;
                        Integer absence = null;

                        if (mouvements.isEmpty()) {
                                // Employé absent toute la journée
                                absence = 1;
                        } else {
                                // Trier mouvements par date
                                mouvements.sort(Comparator.comparing(Presence::getDate));

                                // Séparer entrées et sorties
                                List<Presence> entrees = mouvements.stream()
                                                .filter(p -> "entrée".equalsIgnoreCase(p.getMouvement()))
                                                .toList();

                                List<Presence> sorties = mouvements.stream()
                                                .filter(p -> "sortie".equalsIgnoreCase(p.getMouvement()))
                                                .toList();

                                Presence firstIn = entrees.isEmpty() ? null : entrees.get(0);
                                Presence lastOut = sorties.isEmpty() ? null : sorties.get(sorties.size() - 1);

                                // --- Calcul retard ---
                                if (firstIn != null) {
                                        LocalTime expectedStart = LocalTime.of(8, 0);
                                        LocalTime arrival = firstIn.getDate().toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalTime();
                                        long diff = Duration.between(expectedStart, arrival).toMinutes();
                                        if (diff > 5)
                                                retard = (int) diff;
                                }

                                // --- Calcul heure sup ---
                                if (lastOut != null) {
                                        LocalTime expectedEnd = LocalTime.of(17, 0);
                                        LocalTime departure = lastOut.getDate().toInstant()
                                                        .atZone(ZoneId.systemDefault())
                                                        .toLocalTime();
                                        long extra = Duration.between(expectedEnd, departure).toMinutes();
                                        if (extra > 0)
                                                heureSup = (int) extra;
                                }

                                // --- Calcul pauses supplémentaires ---
                                int totalPauses = 0;
                                for (int i = 0; i < sorties.size(); i++) {
                                        if (i < entrees.size() - 1) {
                                                LocalTime s = sorties.get(i).getDate().toInstant()
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalTime();
                                                LocalTime e = entrees.get(i + 1).getDate().toInstant()
                                                                .atZone(ZoneId.systemDefault())
                                                                .toLocalTime();
                                                long pause = Duration.between(s, e).toMinutes();
                                                if (pause > 60)
                                                        totalPauses += (pause - 60);
                                        }
                                }
                                if (totalPauses > 0)
                                        pauses = totalPauses;
                        }

                        // --- Créer et sauvegarder le HistoMouvement ---
                        HistoMouvement hist = new HistoMouvement(
                                        UUID.randomUUID().toString(),
                                        emp,
                                        retard,
                                        heureSup,
                                        pauses,
                                        absence,
                                        dateToClose);

                        histoRepo.save(hist);
                }
        }

        public Map<String, Object> getAttendanceSummary(int year) {
                Map<String, Object> result = new HashMap<>();
                List<Employe> allEmployees = employeRepository.findAll();

                int[] presents = new int[12]; // index 0 = janvier, 11 = décembre
                int[] absents = new int[12];

                for (int month = 1; month <= 12; month++) {
                        for (Employe emp : allEmployees) {
                                // Récupérer l'historique pour cet employé et ce mois
                                List<HistoMouvement> histos = histoRepo.findByEmployeAndMonthAndYear(emp.getIdEmploye(),
                                                month, year);

                                if (histos.isEmpty()) {
                                        absents[month - 1]++; // pas d'historique → absent
                                } else {
                                        boolean wasAbsent = histos.stream()
                                                        .anyMatch(h -> h.getAbsence() != null && h.getAbsence() == 1);
                                        if (wasAbsent)
                                                absents[month - 1]++;
                                        else
                                                presents[month - 1]++;
                                }
                        }
                }

                result.put("presents", presents);
                result.put("absents", absents);
                return result;
        }

        public List<HistoMouvement> getByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return histoRepo.findByDateBetween(start, end);
        }

        public List<HistoMouvement> getByYear(int year, String idDomaine) {
                if (idDomaine == null || idDomaine.isEmpty()) {
                        return histoRepo.findByYear(year);
                } else {
                        List<Employe> employes = employeRepository.findByDomaine(idDomaine);
                        List<String> ids = employes.stream().map(e -> e.getIdEmploye()).collect(Collectors.toList());
                        return histoRepo.findByYearAndEmployes(year, ids);
                }
        }

        public List<HistoMouvement> getByYearAndMonth(int year, int month, String idDomaine) {
                if (idDomaine == null || idDomaine.isEmpty()) {
                        return histoRepo.findByYearAndMonth(year, month);
                } else {
                        List<Employe> employes = employeRepository.findByDomaine(idDomaine);
                        List<String> ids = employes.stream().map(e -> e.getIdEmploye()).collect(Collectors.toList());
                        return histoRepo.findByYearMonthAndEmployes(year, month, ids);
                }
        }
}
