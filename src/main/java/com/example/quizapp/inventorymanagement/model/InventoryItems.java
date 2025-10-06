package com.example.quizapp.inventorymanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InventoryItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String sku_code;
    private int quantity; 
    private double price;
    private int lowStockThreshold;
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
}
