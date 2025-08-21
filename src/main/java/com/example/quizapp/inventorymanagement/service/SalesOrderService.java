package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final TransactionService transactionService;
    private final InventoryService inventoryService;
    private final InventoryItemRepository inventoryItemRepo;

    public ResponseEntity<String> createSo(String customerName, int quantity, List<SalesOrderItem> salesOrderItemList,User user){
        SalesOrder so = new SalesOrder();
        so.setCustomerName(customerName);
        so.setItems(salesOrderItemList);
        salesOrderItemList.forEach(item -> item.setSalesOrder(so));

        for(SalesOrderItem item : salesOrderItemList){
            InventoryItems inventoryItems = inventoryItemRepo.findByProduct(item.getProduct());
            inventoryService.decreaseStock(item.getProduct(),item.getQuantity());
            transactionService.makeTransaction(inventoryItems,Type.SALES,item.getQuantity(),user,"#SO",null);
        }
        salesOrderRepository.save(so);
        return ResponseEntity.ok("Sales order created successfully");
    }
}
