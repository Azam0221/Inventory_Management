package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.StaticInventoryData;
import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LowStockScheduler {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private NotificationService notificationService;

    public LowStockScheduler(InventoryService inventoryService,NotificationService notificationService){
        this.inventoryService = inventoryService;
        this.notificationService = notificationService;
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndNotifyLowStock() throws JsonProcessingException {
       // List<InventoryItems> lowStockItems = inventoryService.getLowStockItems().getBody();
        List<InventoryItems> lowStockItems =  Collections.emptyList();// StaticInventoryData.getLowStockItems();
        assert lowStockItems != null;
        //if(true){
           notificationService.sendLowStockAlert(lowStockItems);
      // }
    }
}
