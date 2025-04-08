package com.example.coconsult.services;
import com.example.coconsult.Repository.RoleRepository;
import com.example.coconsult.Repository.UserRepository;
import com.example.coconsult.entities.Role;
import com.example.coconsult.entities.User;
import com.example.coconsult.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getRoleById(String id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role updateRole(String id, Role role) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isPresent()) {
            Role updatedRole = existingRole.get();
            // Mettre à jour le nom
            if (role.getName() != null) {
                updatedRole.setName(role.getName());
            }
            // Mettre à jour la liste des users si fournie
            if (role.getUsers() != null) {
                updatedRole.setUsers(role.getUsers());
            }
            return roleRepository.save(updatedRole);
        }
        throw new RuntimeException("Role not found with id: " + id);
    }

    @Override
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role addUserToRole(String roleId, String userId) {
        log.debug("Attempting to add user {} to role {}", userId, roleId);

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        log.debug("Role found: {}", roleOpt.isPresent());

        Optional<User> userOpt = userRepository.findById(userId);
        log.debug("User found: {}", userOpt.isPresent());

        if (roleOpt.isEmpty()) {
            log.warn("Role with ID {} not found", roleId);
            throw new RuntimeException("Role not found with ID: " + roleId);
        }

        if (userOpt.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // À ce stade, on est sûr que les deux entités existent
        Role role = roleOpt.get();
        User user = userOpt.get();

        role.addUser(user);
        user.addRole(roleId);
        userRepository.save(user);
        log.debug("User {} added to role {}", userId, roleId);

        return roleRepository.save(role);
    }

    @Override
    public Role removeUserFromRole(String roleId, String userId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (roleOpt.isPresent() && userOpt.isPresent()) {
            Role role = roleOpt.get();
            User user = userOpt.get();
            role.removeUser(user);
            return roleRepository.save(role);
        }
        throw new RuntimeException("Role or User not found");
    }
}