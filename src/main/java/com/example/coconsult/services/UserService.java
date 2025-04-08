package com.example.coconsult.services;


import com.example.coconsult.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    User updateUser(String userId, User userDetails);

    List<User> getAllUsers();

    User getUserById(String userId);

    User getUserByIdentifiant(String identifiant);

    User getUserByEmail(String email);

    void deleteUser(String userId);

    boolean existsByIdentifiant(String identifiant);

    boolean existsByEmail(String email);

    User assignRoleToUser(String userId, String roleName);

    User removeRoleFromUser(String userId, String roleName);

    // Méthodes pour Spring Security
    User findUserByUsername(String username);

    void changePassword(String userId, String oldPassword, String newPassword);
}