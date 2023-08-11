package com.halfacode.service;

import com.halfacode.dto.ApiResponse;
import com.halfacode.entity.Role;
import com.halfacode.exception.RoleCreationException;
import com.halfacode.repoistory.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public ApiResponse<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return new ApiResponse<>(200, roles, "Roles retrieved successfully");
    }

    public ApiResponse<Role> getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return new ApiResponse<>(200, role.get(), "Role retrieved successfully");
        } else {
            return new ApiResponse<>(404, null, "Role not found");
        }
    }

    public ApiResponse<Role> createRole(Role role) {
        String roleName = role.getName();

        // Check if a role with the same name already exists
        Optional<Role> existingRole = roleRepository.findByName(roleName);

        if (existingRole.isPresent()) {
            throw new RoleCreationException("Role with the same name already exists");
        }

        // Role with the same name doesn't exist, create the role
        Role createdRole = roleRepository.save(role);
        return new ApiResponse<>(201, createdRole, "Role created successfully");
    }

    public ApiResponse<Role> updateRole(Long id, Role role) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isPresent()) {
            role.setId(id); // Set the role ID before updating
            Role updatedRole = roleRepository.save(role); // Update the role
            return new ApiResponse<>(200, updatedRole, "Role updated successfully");
        } else {
            return new ApiResponse<>(404, null, "Role not found");
        }
    }

    public ApiResponse<Void> deleteRole(Long id) {
        boolean deleted = roleRepository.existsById(id);
        if (deleted) {
            roleRepository.deleteById(id);
            return new ApiResponse<>(204, null, "Role deleted successfully");
        } else {
            return new ApiResponse<>(404, null, "Role not found");
        }
    }
}