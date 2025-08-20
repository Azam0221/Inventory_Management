package com.example.quizapp.inventorymanagement.model;


import com.example.quizapp.inventorymanagement.enum_.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Supplier supplier;

    private LocalDateTime orderDate;
    private Status status;

    @OneToMany(mappedBy ="purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderItem> purchaseOrderItems;
}


