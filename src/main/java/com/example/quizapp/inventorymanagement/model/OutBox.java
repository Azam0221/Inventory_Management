package com.example.quizapp.inventorymanagement.model;


import com.example.quizapp.inventorymanagement.enum_.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
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

    private Status status = Status.PENDING;
    private Integer attempts;

    private Instant createdAt;
    private Instant lastProcessedAt;


}
