package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.enum_.Role;
import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.NotificationRuleRepository;
import com.example.quizapp.inventorymanagement.repository.OutBoxRepository;
import com.example.quizapp.inventorymanagement.repository.ResponsibilityRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {


    private final JavaMailSender mailSender;
    private UserRepository userRepo;
    private ResponsibilityRepository responsibilityRepo;
    private NotificationRuleRepository notificationRuleRepo;
    private final OutBoxRepository outBoxRepo;
    private final ObjectMapper mapper;



    public NotificationService(JavaMailSender mailSender,
                               UserRepository userRepository,
                               ResponsibilityRepository responsibilityRepository,
                               NotificationRuleRepository notificationRuleRepository,
                               OutBoxRepository outBoxRepository,
                               ObjectMapper objectMapper) {
        this.mailSender = mailSender;
        this.userRepo = userRepository;
        this.notificationRuleRepo = notificationRuleRepository;
        this.responsibilityRepo = responsibilityRepository;
        this.outBoxRepo = outBoxRepository;
        this.mapper = objectMapper;

    }

    public void notifyEvent(String eventType, String condition,String messageBody) throws JsonProcessingException {
        List<NotificationRule> rules = notificationRuleRepo.findByEventType(eventType);

        for (NotificationRule rule : rules) {

            List<User> targets = resolveTargets(rule);
            for(User user: targets){
                NotificationEvent event = new NotificationEvent();
                event.setId(UUID.randomUUID().toString());
                event.setEventType(eventType);
                event.setBody(messageBody);
                event.setRecipient(user.getEmail());

                String payload = mapper.writeValueAsString(event);

                // Write the code for the outbox builder

                if(rule.getChannels().contains("Email")){
                    sendMail(user.getEmail(),eventType,messageBody);
                }
                else{
                    System.out.println("SMS not yet implemented");
                }
            }
        }
    }

        public List<User> resolveTargets(NotificationRule rule){
        switch (rule.getTargetType()){
            case USER:
                return userRepo.findById((long) Integer.parseInt(rule.getTargetValue()))
                        .map(List::of).orElse(List.of());
            case ROLE:
                return userRepo.findByRole(Role.valueOf(rule.getTargetValue()));

            case RESPONSIBILITY:
                return responsibilityRepo.findByName(rule.getTargetValue())
                        .map(res -> userRepo.findByResponsibilitiesContaining(res))
                        .orElse(List.of());
            default:
                return List.of();
        }
        }


    public void sendLowStockAlert(List<InventoryItems> items) throws JsonProcessingException {
        String subject = "Low Stock Alert";
        StringBuilder body = new StringBuilder("The following items are low in stock:\n\n");

        for(InventoryItems item: items){
            body.append(item.getProduct().getName())
                    .append(" SKU ").append(item.getSku_code())
                    .append(") - Qty:").append(item.getQuantity())
                    .append("\n");
        }
        NotificationEvent event = new NotificationEvent();

        event.setId(UUID.randomUUID().toString());
        event.setEventType("Email");
        event.setBody(body.toString());
        event.setRecipient("mohdazam022102@gmail.com");
        event.setOccurredOn(Instant.now().toEpochMilli());

        String payload = mapper.writeValueAsString(event);

        OutBox outbox = new OutBox();
        outbox.setAggregateId(event.getId());
        outbox.setAggregateType("NotificationEvent");
        outbox.setEventType(event.getEventType());
        outbox.setPayLoad(payload);
        outbox.setCreatedAt(Instant.now());
        outbox.setStatus(Status.PENDING);
        outbox.setAttempts(0);

        outBoxRepo.save(outbox);

       // sendMail("mohdazam022102@gmail.com",subject,body.toString());

    }

    public void sendMail(String to,String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
