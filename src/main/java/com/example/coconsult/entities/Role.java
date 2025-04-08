package com.example.coconsult.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    private String name;

    @DBRef
    private List<User> users = new ArrayList<>();

    public Role(String name) {
        this.name = name;
    }

    // Méthode pour ajouter un utilisateur au rôle
    public void addUser(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }

        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }

    // Méthode pour supprimer un utilisateur du rôle
    public void removeUser(User user) {
        if (this.users != null) {
            this.users.remove(user);
        }
    }

    public String getName() {
        return name;
    }
}