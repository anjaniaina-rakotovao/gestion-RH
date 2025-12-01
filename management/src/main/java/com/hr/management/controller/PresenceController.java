package com.hr.management.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.management.entities.Domaine;
import com.hr.management.entities.Employe;
import com.hr.management.entities.HistoMouvement;
import com.hr.management.entities.Presence;
import com.hr.management.service.DomaineService;
import com.hr.management.service.HistoMouvementService;
import com.hr.management.service.PresenceService;


@RestController
@RequestMapping("api/presence")
public class PresenceController {

    private final PresenceService presenceService;
    private final HistoMouvementService histoMouvementService;
    private final DomaineService domaineService;

    private final List<Employe> allEmployees;

    public PresenceController(
            PresenceService presenceService,
            HistoMouvementService histoMouvementService,
            DomaineService domaineService
        ) {
        this.presenceService = presenceService;
        this.histoMouvementService = histoMouvementService;
        this.domaineService = domaineService;
        this.allEmployees = new ArrayList<>();
    }

    @PostMapping("/{idEmploye}/{dateTime}/{mouvement}/add")
    public Presence addPresence(
            @PathVariable String idEmploye,
            @PathVariable String dateTime,
            @PathVariable String mouvement) {
        Presence p = new Presence();
        p.setIdPresence(UUID.randomUUID().toString());

        Employe emp = new Employe();
        emp.setIdEmploye(idEmploye);
        p.setEmploye(emp);

        // Convertir la date ISO : 2025-03-01T08:10:00
        try {
            Instant instant = Instant.parse(dateTime);
            p.setDate(Date.from(instant));
        } catch (Exception e) {
            throw new RuntimeException("Format dateTime invalide. Utilise format ISO : 2025-03-01T08:10:00");
        }

        p.setMouvement(mouvement);

        return presenceService.save(p);
    }

    // GET /api/presence/journey?date=2025-03-01
    @GetMapping("/journey")
    public List<Presence> getPresenceByDay(@RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);

        Date start = Date.from(localDate.atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant());

        Date end = Date.from(localDate.plusDays(1)
                .atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant());

        return presenceService.getMovementsForDay(start, end);
    }

    // POST /api/presence/close?date=2025-03-01
    @PostMapping("/close")
    public Map<String, Object> closeDay(@RequestParam String date) {

        Map<String, Object> response = new HashMap<>();
        LocalDate day;
        try {
            day = LocalDate.parse(date);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Format de date invalide. Utiliser YYYY-MM-DD");
            return response;
        }

        // Appel du service pour calculer et enregistrer l'historique
        histoMouvementService.closeJourney(day);

        response.put("status", "OK");
        response.put("message", "Journée clôturée avec succès");
        response.put("date", day);
        return response;
    }

    @GetMapping("/attendance-summary")
    public Map<String, List<Integer>> getAttendanceSummary(@RequestParam int year) {

        Map<String, List<Integer>> result = new HashMap<>();

        // Initialiser les tableaux à 0
        List<Integer> presents = new ArrayList<>(Collections.nCopies(12, 0));
        List<Integer> absents  = new ArrayList<>(Collections.nCopies(12, 0));

        // Récupérer tous les HistoMouvement de l'année
        List<HistoMouvement> mouvements = histoMouvementService.getByYear(year);

        for (HistoMouvement h : mouvements) {
            int month = h.getDate().getMonthValue() - 1; // janvier=0
            if (h.getAbsence() != null && h.getAbsence() == 1) {
                absents.set(month, absents.get(month) + 1);
            } else {
                presents.set(month, presents.get(month) + 1);
            }
        }

        result.put("presents", presents);
        result.put("absents", absents);

        return result;
    }

    @GetMapping("/summary")
    public Map<String, Object> getSummary(
        @RequestParam int year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) String domaine
    ) {
        Map<String, Object> result = new HashMap<>();
        List<HistoMouvement> mouvements;

        if (month == null) {
            mouvements = histoMouvementService.getByYear(year, domaine);

            String[] labels = { "Janvier","Février","Mars","Avril","Mai","Juin",
                                "Juillet","Août","Septembre","Octobre","Novembre","Décembre" };

            int[] present = new int[12];
            int[] absent = new int[12];

            for (HistoMouvement h : mouvements) {
                int m = h.getDate().getMonthValue() - 1;
                if (h.getAbsence() != null && h.getAbsence() == 1) absent[m]++;
                else present[m]++;
            }

            result.put("labels", labels);
            result.put("present", present);
            result.put("absent", absent);

        } else {
            mouvements = histoMouvementService.getByYearAndMonth(year, month, domaine);

            LocalDate firstDay = LocalDate.of(year, month, 1);
            int length = firstDay.lengthOfMonth();

            String[] labels = new String[length];
            int[] present = new int[length];
            int[] absent = new int[length];

            for (int i = 0; i < length; i++) labels[i] = String.valueOf(i + 1);

            for (HistoMouvement h : mouvements) {
                int d = h.getDate().getDayOfMonth() - 1;
                if (h.getAbsence() != null && h.getAbsence() == 1) absent[d]++;
                else present[d]++;
            }

            result.put("labels", labels);
            result.put("present", present);
            result.put("absent", absent);
        }

        return result;
    }
    
}
