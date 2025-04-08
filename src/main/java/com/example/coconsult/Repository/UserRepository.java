package com.example.coconsult.Repository;

import com.example.coconsult.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByIdentifiant(String identifiant);

    Optional<User> findByEmail(String email);

    boolean existsByIdentifiant(String identifiant);

    boolean existsByEmail(String email);
}
