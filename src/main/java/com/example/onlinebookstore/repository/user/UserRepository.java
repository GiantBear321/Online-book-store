package com.example.onlinebookstore.repository.user;

import com.example.onlinebookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //find by email
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);
}
