package com.example.quizapp.inventorymanagement.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    private Product product;

    private int quantity;
    private double price;

}
 