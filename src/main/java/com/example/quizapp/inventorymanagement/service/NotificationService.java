package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.enum_.Role;
import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.NotificationRule;
import com.example.quizapp.inventorymanagement.model.User;
import com.example.quizapp.inventorymanagement.repository.NotificationRuleRepository;
import com.example.quizapp.inventorymanagement.repository.ResponsibilityRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {


    private final JavaMailSender mailSender;
    private UserRepository userRepo;
    private ResponsibilityRepository responsibilityRepo;
    private NotificationRuleRepository notificationRuleRepo;


    public NotificationService(JavaMailSender mailSender,
                               UserRepository userRepository,
                               ResponsibilityRepository responsibilityRepository,
                               NotificationRuleRepository notificationRuleRepository) {
        this.mailSender = mailSender;
        this.userRepo = userRepository;
        this.notificationRuleRepo = notificationRuleRepository;
        this.responsibilityRepo = responsibilityRepository;
    }

    public void notifyEvent(String eventType, String condition,String messageBody) {
        List<NotificationRule> rules = notificationRuleRepo.findByEventType(eventType);

        for (NotificationRule rule : rules) {

            List<User> targets = resolveTargets(rule);
            for(User user: targets){
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

    public void sendLowStockAlert(List<InventoryItems> items){
        String subject = "Low Stock Alert";
        StringBuilder body = new StringBuilder("The following items are low in stock:\n\n");

        for(InventoryItems item: items){
            body.append(item.getProduct().getName())
                    .append(" SKU ").append(item.getSku_code())
                    .append(") - Qty:").append(item.getQuantity())
                    .append("\n");
        }

        sendMail("mohdazam022102@gmail.com",subject,body.toString());

    }

    public void sendMail(String to,String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
