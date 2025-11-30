// package com.hr.management.service;

// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.hr.management.entities.Evaluation;
// import com.hr.management.entities.RapportPerformance;
// import com.hr.management.repository.EvaluationRepository;
// import com.hr.management.repository.RapportPerformanceRepository;

// @Service
// public class RapportService {
//     private final EvaluationRepository evaluationRepository;
//     private final RapportPerformanceRepository rapportRepo;
//     public RapportService(EvaluationRepository evaluationRepository,
//                           RapportPerformanceRepository rapportRepo) {
//         this.evaluationRepository = evaluationRepository;
//         this.rapportRepo = rapportRepo;
//     }
//     public RapportPerformance genererRapportEntreprise(String periode) {
//         List<Evaluation> evs = evaluationRepository.findByPeriode(periode);
//         String path = "/tmp/rapport-" + periode + ".csv";
//         // écrire CSV -> FileWriter...
//         RapportPerformance r = new RapportPerformance();
//         r.setPeriode(periode);
//         r.setTypeRapport("Entreprise");
//         r.setFichierPath(path);
//         r.setDateGeneration(LocalDateTime.now());
//         return rapportRepo.save(r);
//     }
// }



