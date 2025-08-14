package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.model.InventoryItems;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {


    private final JavaMailSender mailSender;


    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendLowStockAlert(List<InventoryItems> items){
        String subject = "Low Stock Alert";
        StringBuilder body = new StringBuilder("The following items are low in stock:\n\n");

        for(InventoryItems item: items){
            body.append(item.getName())
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
