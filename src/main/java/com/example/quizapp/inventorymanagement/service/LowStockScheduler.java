package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.model.InventoryItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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


    @Scheduled(cron = "0 0 8 ? * MON-FRI")
    public void checkAndNotifyLowStock(){
        List<InventoryItems> lowStockItems = inventoryService.getLowStockItems().getBody();
        assert lowStockItems != null;
        if(!lowStockItems.isEmpty()){
           notificationService.sendLowStockAlert(lowStockItems);
       }
    }
}
