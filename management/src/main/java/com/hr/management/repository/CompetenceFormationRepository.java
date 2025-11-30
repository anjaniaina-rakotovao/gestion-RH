package com.hr.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hr.management.entities.Formation;
import com.hr.management.entities.FormationCompetence;
import com.hr.management.entities.FormationCompetenceId;

public interface CompetenceFormationRepository extends JpaRepository<FormationCompetence, FormationCompetenceId> {

    List<Formation> findAllByIdCompetence(Integer idCompetence);

   // Dans CompetenceFormationRepository
@Query(value="SELECT fc.formation FROM FormationCompetence fc WHERE fc.idCompetence = :competenceId")
public abstract List<Formation> findFormationsByCompetenceId(@Param(value="competenceId") Integer competenceId);
// Le retour est maintenant List<Formation> et est compatible.

}
