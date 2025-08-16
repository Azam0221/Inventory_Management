package com.example.quizapp.inventorymanagement.model;


import lombok.Data;

@Data
public class StockAdjustmentRequest {


    private int adjustment;
    private String remarks;

}
