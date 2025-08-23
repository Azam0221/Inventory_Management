package com.example.quizapp.inventorymanagement.model;


import lombok.Data;

import java.util.List;

@Data
public class SalesOrderRequest {

    private String customerName;
    List<SalesOrderItem> salesOrderItems;
}
