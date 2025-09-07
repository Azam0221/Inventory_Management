package com.example.quizapp.inventorymanagement.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {

    private String id;
    private String eventType;
    private String recipient;
    private String subject;
    private String body;
    private long occurredOn;
}
