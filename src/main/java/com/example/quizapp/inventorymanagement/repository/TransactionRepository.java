package com.example.quizapp.inventorymanagement.repository;


import com.example.quizapp.inventorymanagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

}
