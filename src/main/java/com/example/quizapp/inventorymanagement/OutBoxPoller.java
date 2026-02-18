package com.example.quizapp.inventorymanagement;


import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.model.OutBox;
import com.example.quizapp.inventorymanagement.repository.OutBoxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutBoxPoller {

    private final OutBoxRepository outBoxRepository;
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final PlatformTransactionManager txManager;

    @Scheduled(fixedDelayString = "${outbox.poll.interval.ms:500}")
    public void pollAndPublish(){
        List<OutBox> pending = outBoxRepository.findPendingEventsForProcessing();

        for(OutBox outBox : pending){
            try {
                kafkaTemplate.send("notification.events",outBox.getEventType(),
                        outBox.getPayLoad());
                outBox.setStatus(Status.PUBLISHED);
                outBox.setLastProcessedAt(Instant.now());
                outBoxRepository.save(outBox);

            }
            catch (Exception e){
                outBox.setAttempts(outBox.getAttempts()+1);
                outBox.setLastProcessedAt(Instant.now());
                if(outBox.getAttempts() > 5){
                    outBox.setStatus(Status.FAILED);
                }
                outBoxRepository.save(outBox);
            }
        }
    }
}
