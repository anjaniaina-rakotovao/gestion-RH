package com.hr.management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hr.management.entities.Role;
import com.hr.management.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository repo;

    public RoleService(RoleRepository repo) {
        this.repo = repo;
    }

    public List<Role> getAllRoles() {
        return repo.findAll();
    }

    public Role getById(String id) {
        return repo.findById(id).orElse(null);
    }
}
