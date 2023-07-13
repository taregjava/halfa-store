package com.halfacode.service;


import com.halfacode.entity.Role;
import com.halfacode.entity.User;
import com.halfacode.repoistory.RoleRepository;
import com.halfacode.repoistory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        Collection<Role> roles = user.getRoles();
        Set<Role> resolvedRoles = new HashSet<>();
        for (Role role : roles) {
            Optional<Role> resolvedRole = roleRepository.findById(role.getId());
            resolvedRole.ifPresent(resolvedRoles::add);
        }
        user.setRoles(resolvedRoles);
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        } else {
            return false;
        }
    }
}
