package com.example.quizapp.inventorymanagement.model;


import com.example.quizapp.inventorymanagement.enum_.TargetType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class NotificationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String eventType;

    private String conditionExpression;

    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    private String targetValue;

    private String channels;



}
