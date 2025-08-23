package com.example.quizapp.inventorymanagement.controller;


import com.example.quizapp.inventorymanagement.model.SalesOrderRequest;
import com.example.quizapp.inventorymanagement.service.SalesOrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sales-order")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping("create")
    public ResponseEntity<String> createSO(@RequestBody SalesOrderRequest request){
        return salesOrderService.createSo(request.getCustomerName(),request.getSalesOrderItems());
    }

}
