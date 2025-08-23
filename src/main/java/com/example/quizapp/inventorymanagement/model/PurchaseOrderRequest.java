package com.example.quizapp.inventorymanagement.model;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderRequest {

    private Supplier supplier;
    private List<PurchaseOrderItem> purchaseOrderItems;
}
