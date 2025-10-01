package com.example.quizapp.inventorymanagement.service;

import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.SalesOrderRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final TransactionService transactionService;
    private final InventoryService inventoryService;
    private final InventoryItemRepository inventoryItemRepo;
    private final UserRepository userRepository;

    public ResponseEntity<String> createSo(String customerName, List<SalesOrderItem> salesOrderItemList){
        SalesOrder so = new SalesOrder();
        so.setCustomerName(customerName);
        so.setItems(salesOrderItemList);
        salesOrderItemList.forEach(item -> item.setSalesOrder(so));
        User currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        for(SalesOrderItem item : salesOrderItemList){
            InventoryItems inventoryItems = inventoryItemRepo.findByProduct(item.getProduct());
            inventoryService.decreaseStock(item.getProduct(),item.getQuantity());
            transactionService.makeTransaction(inventoryItems,Type.SALES,item.getQuantity(),currentUser,"#SO",null);
        }
        salesOrderRepository.save(so);
        return ResponseEntity.ok("Sales order created successfully");
    }
}
