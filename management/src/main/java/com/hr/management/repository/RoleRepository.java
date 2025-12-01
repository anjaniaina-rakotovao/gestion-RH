package com.hr.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hr.management.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String> {

}
