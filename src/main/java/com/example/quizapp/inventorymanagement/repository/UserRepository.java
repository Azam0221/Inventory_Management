package com.example.quizapp.inventorymanagement.repository;


import com.example.quizapp.inventorymanagement.enum_.Role;
import com.example.quizapp.inventorymanagement.model.Responsibility;
import com.example.quizapp.inventorymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String username);
    boolean existsByEmail(String email);

    User findByName(String name);

    List<User> findByRole(Role role);

    List<User> findByResponsibilitiesContaining(Responsibility responsibilities);
}
