package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.model.OutBox;
import com.example.quizapp.inventorymanagement.repository.OutBoxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutBoxService {
    private final OutBoxRepository outBoxRepo;
    public final ObjectMapper objectMapper;

    public void publishEvent(String aggregateType,String agrregateId,String eventType,Object payload){

        try {
            OutBox outBox = new OutBox();
            outBox.setAggregateId(agrregateId);
            outBox.setAggregateType(aggregateType);
            outBox.setEventType(eventType);
            outBox.setPayLoad(objectMapper.writeValueAsString(payload));
            outBoxRepo.save(outBox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload",e);
        }
    }
}
