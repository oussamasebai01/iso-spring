package com.example.coconsult.Repository;

import com.example.coconsult.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByIdentifiant(String identifiant);

    Optional<User> findByEmail(String email);

    boolean existsByIdentifiant(String identifiant);

    boolean existsByEmail(String email);
    List<User> findByDepartmentId(String department);

    List<User> findByEmploymentStatus(String employmentStatus);
    List<User> findByDepartmentIdAndEmploymentStatus(String department, String employmentStatus);
   // List<User> findByDepartmentIdAndEmploymentStatus(String departmentId, String employmentStatus);
}
