package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepo;
    private final InventoryService inventoryService;
    private final TransactionService transactionService;
    private final InventoryItemRepository inventoryItemRepo;

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

    public ResponseEntity<String> receivePo(Long poId, User user){
        PurchaseOrder po = poRepo.findById(poId).get();
        if( po.getStatus() != Status.APPROVED){
            return new ResponseEntity<>("Purchase order is not approved", HttpStatus.BAD_REQUEST);
        }

        po.setStatus(Status.RECEIVED);
        for(PurchaseOrderItem item: po.getPurchaseOrderItems()){
            InventoryItems inventoryItems = inventoryItemRepo.findByProduct(item.getProduct());
            inventoryService.increaseStock(item.getProduct(),item.getQuantity(),po.getSupplier());
            transactionService.makeTransaction(inventoryItems, Type.ADD,item.getQuantity(),user,"#PO",po.getSupplier());
        }
        poRepo.save(po);

        return new ResponseEntity<>("Purchase order received successfully",HttpStatus.OK);

    }
}
