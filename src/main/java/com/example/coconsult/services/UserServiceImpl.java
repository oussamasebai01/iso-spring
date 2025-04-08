package com.example.coconsult.services;


import com.example.coconsult.Repository.RoleRepository;
import com.example.coconsult.Repository.UserRepository;

import com.example.coconsult.entities.Role;
import com.example.coconsult.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        // Vérifier si l'identifiant ou l'email existent déjà
        if (existsByIdentifiant(user.getIdentifiant())) {
            throw new RuntimeException("Identifiant déjà utilisé: " + user.getIdentifiant());
        }

        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé: " + user.getEmail());
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assigner le rôle USER par défaut si aucun rôle n'est défini
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            List<String> defaultRoles = new ArrayList<>();
            defaultRoles.add("USER");
            user.setRoles(defaultRoles);
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, User userDetails) {
        User user = getUserById(userId);

        // Mettre à jour les informations de base
        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setExperience(userDetails.getExperience());

        // Vérifier si l'email est modifié et s'il est unique
        if (!user.getEmail().equals(userDetails.getEmail())) {
            if (existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email déjà utilisé: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        // Vérifier si l'identifiant est modifié et s'il est unique
        if (!user.getIdentifiant().equals(userDetails.getIdentifiant())) {
            if (existsByIdentifiant(userDetails.getIdentifiant())) {
                throw new RuntimeException("Identifiant déjà utilisé: " + userDetails.getIdentifiant());
            }
            user.setIdentifiant(userDetails.getIdentifiant());
        }

        // Ne pas mettre à jour le mot de passe ici (utiliser changePassword)

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
    }

    @Override
    public User getUserByIdentifiant(String identifiant) {
        return userRepository.findByIdentifiant(identifiant)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'identifiant: " + identifiant));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
    }

    @Override
    public void deleteUser(String userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public boolean existsByIdentifiant(String identifiant) {
        return userRepository.existsByIdentifiant(identifiant);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User assignRoleToUser(String userId, String roleName) {
        User user = getUserById(userId);

        // Vérifier si le rôle existe
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé: " + roleName));

        // Ajouter le rôle à l'utilisateur s'il ne l'a pas déjà
        if (!user.getRoles().contains(roleName)) {
            user.addRole(roleName);

            // Mettre à jour la relation bidirectionnelle
            role.addUser(user);
            roleRepository.save(role);
        }

        return userRepository.save(user);
    }

    @Override
    public User removeRoleFromUser(String userId, String roleName) {
        User user = getUserById(userId);

        // Vérifier si l'utilisateur a le rôle
        if (user.getRoles().contains(roleName)) {
            user.getRoles().remove(roleName);

            // Mettre à jour la relation bidirectionnelle
            Optional<Role> roleOpt = roleRepository.findByName(roleName);
            if (roleOpt.isPresent()) {
                Role role = roleOpt.get();
                role.removeUser(user);
                roleRepository.save(role);
            }
        }

        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        // Dans ce cas, l'identifiant est utilisé comme nom d'utilisateur pour Spring Security
        return userRepository.findByIdentifiant(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + username));
    }

    @Override
    public void changePassword(String userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);

        // Vérifier que l'ancien mot de passe est correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mot de passe actuel incorrect");
        }

        // Encoder et enregistrer le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}