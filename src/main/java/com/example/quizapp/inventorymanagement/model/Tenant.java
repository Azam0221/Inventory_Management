package com.example.quizapp.inventorymanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tenantId;

    @Column(nullable = false,unique = true)
    private String tenantName;
}
