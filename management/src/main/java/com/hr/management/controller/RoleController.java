package com.hr.management.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.hr.management.entities.Role;
import com.hr.management.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Role> getAll() {
        return service.getAllRoles();
    }
}
