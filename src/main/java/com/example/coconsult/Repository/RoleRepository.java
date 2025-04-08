package com.example.coconsult.Repository;

import com.example.coconsult.entities.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role,String> {
    /**
     * Trouve un rôle par son nom
     *
     * @param name Le nom du rôle à rechercher
     * @return Un Optional contenant le rôle s'il existe
     */
    Optional<Role> findByName(String name);

    /**
     * Vérifie si un rôle avec le nom spécifié existe
     *
     * @param name Le nom du rôle à vérifier
     * @return true si le rôle existe, false sinon
     */
    boolean existsByName(String name);
}
