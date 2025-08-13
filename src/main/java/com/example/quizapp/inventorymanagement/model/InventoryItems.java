package com.example.quizapp.inventorymanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InventoryItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String sku_code;
    private String name;
    private String category;
    private String description;
    private int quantity;
    private double price;
    private String supplierName;
    private String supplierContact;
    private String lowStockThreshold;
    private boolean active = true;
}
