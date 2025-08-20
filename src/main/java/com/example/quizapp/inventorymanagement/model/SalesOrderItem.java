package com.example.quizapp.inventorymanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class SalesOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private SalesOrder salesOrder;

    @ManyToOne
    private Product product;

    private int quantity;
    private double price;
}
