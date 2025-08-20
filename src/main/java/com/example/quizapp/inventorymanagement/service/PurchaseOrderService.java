package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.model.PurchaseOrder;
import com.example.quizapp.inventorymanagement.model.PurchaseOrderItem;
import com.example.quizapp.inventorymanagement.model.Supplier;
import com.example.quizapp.inventorymanagement.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final InventoryService inventoryservice;
    private final TransactionService transactionService;

    public ResponseEntity<String> createPo(Supplier supplier, List<PurchaseOrderItem> items){
        if(supplier == null || items == null || items.isEmpty()){
            return ResponseEntity.badRequest().body("Supplier or items are empty");
        }
        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(supplier);
        po.setOrderDate(LocalDateTime.now());
        po.setStatus(Status.PENDING);
        po.setPurchaseOrderItems(items);
        poRepo.save(po);

        return ResponseEntity.ok("Purchase order created successfully");
    }

    public ResponseEntity<String> approvePo(Long id){
        PurchaseOrder po = poRepo.findById(id).get();
        po.setStatus(Status.APPROVED);
        poRepo.save(po);
        return ResponseEntity.ok("Purchase order approved successfully");
    }
}
