package com.example.quizapp.inventorymanagement.repository;


import com.example.quizapp.inventorymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String username);
    boolean existsByEmail(String email);

    User findByName(String name);
}
