package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.hr.management.entities.FormationCompetence;
import com.hr.management.entities.FormationCompetenceId;
public interface FormationCompetenceRepository extends JpaRepository<FormationCompetence, FormationCompetenceId> {
    @Query("SELECT fc FROM FormationCompetence fc WHERE fc.idCompetence = :idCompetence")
    List<FormationCompetence> findAllByIdCompetence(@Param("idCompetence") Integer idCompetence);

    @Query("SELECT fc FROM FormationCompetence fc WHERE fc.idCompetence = :competenceId")
    List<FormationCompetence> findByCompetenceId(@Param("competenceId") Integer idCompetence);
}
