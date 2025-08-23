package com.example.quizapp.inventorymanagement.controller;


import com.example.quizapp.inventorymanagement.model.PurchaseOrderRequest;
import com.example.quizapp.inventorymanagement.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase-order")
@RequiredArgsConstructor
public class PurchaseOderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    public ResponseEntity<String> createPO(@RequestBody PurchaseOrderRequest request){
        return purchaseOrderService.createPo(request.getSupplier(),request.getPurchaseOrderItems());
    }

    @PutMapping("{id}/approve")
    public ResponseEntity<String> approvePo(@PathVariable Long id){
        return purchaseOrderService.approvePo(id);
    }

    @PutMapping("{id}/receive")
    public ResponseEntity<String> receivePo(@PathVariable Long id){
        return purchaseOrderService.receivePo(id);
    }
}
