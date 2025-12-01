package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.DocumentArchive;

public interface DocumentArchiveRepository extends JpaRepository<DocumentArchive, String> {

}
