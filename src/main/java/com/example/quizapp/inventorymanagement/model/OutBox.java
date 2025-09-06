package com.example.quizapp.inventorymanagement.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "notification_outbox")
@Data
public class OutBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private String aggregateId;
    private String eventType;

    @Column(columnDefinition = "json")
    private String payLoad;

    private String status = "PENDING";
    private Integer attempts;


    private LocalDateTime createdAt = LocalDateTime.now();

}
