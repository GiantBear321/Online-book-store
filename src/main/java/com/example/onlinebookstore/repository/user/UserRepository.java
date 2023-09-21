package com.example.onlinebookstore.repository.user;

import com.example.onlinebookstore.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    //find by email
    @Query("FROM User u LEFT JOIN FETCH u.roles r "
            + "WHERE u.email = :email "
            + "AND u.isDeleted = FALSE "
            + "AND r.isDeleted = FALSE")
    Optional<User> findByEmail(String email);
}
