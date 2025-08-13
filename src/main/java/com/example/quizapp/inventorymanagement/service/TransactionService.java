package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.Transaction;
import com.example.quizapp.inventorymanagement.model.User;
import com.example.quizapp.inventorymanagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService{

    @Autowired
    private TransactionRepository transactionRepo;

    public void makeTransaction(InventoryItems inventoryItems, Type type, int quantityChange, User user,String remarks){
        Transaction tx = new Transaction();
        tx.setInventoryItems(inventoryItems);
        tx.setType(type);
        tx.setQuantityChange(quantityChange);
        tx.setUser(user);
        tx.setTimeStamp(LocalDateTime.now());
        tx.setRemarks(remarks);

        transactionRepo.save(tx);
    }
}
