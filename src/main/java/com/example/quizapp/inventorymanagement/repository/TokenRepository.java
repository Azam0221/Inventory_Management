package com.example.quizapp.inventorymanagement.repository;



import com.example.quizapp.inventorymanagement.model.Token;
import com.example.quizapp.inventorymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    List<Token> findAllValidTokensByUser(User user);
}
