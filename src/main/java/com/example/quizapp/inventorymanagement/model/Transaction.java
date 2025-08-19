package com.example.quizapp.inventorymanagement.model;


import com.example.quizapp.inventorymanagement.enum_.Type;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private InventoryItems inventoryItems;

    @Enumerated(EnumType.STRING)
    private Type type;

    private int quantityChange;
    private LocalDateTime timeStamp;

    @ManyToOne
    private User user;


    @ManyToOne
    @JoinColumn(name = "supplier_id",nullable = true)
    private Supplier supplier;

    private String remarks;

}
