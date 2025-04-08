package com.example.coconsult.services;

import com.example.coconsult.entities.Role;
import java.util.List;
import java.util.Optional;

public interface RoleService {
    // Créer un nouveau rôle
    Role createRole(Role role);

    // Récupérer tous les rôles
    List<Role> getAllRoles();

    // Récupérer un rôle par ID
    Optional<Role> getRoleById(String id);

    // Récupérer un rôle par nom
    Optional<Role> getRoleByName(String name);

    // Mettre à jour un rôle
    Role updateRole(String id, Role role);

    // Supprimer un rôle
    void deleteRole(String id);

    // Ajouter un utilisateur à un rôle
    Role addUserToRole(String roleId, String userId);

    // Retirer un utilisateur d'un rôle
    Role removeUserFromRole(String roleId, String userId);
}