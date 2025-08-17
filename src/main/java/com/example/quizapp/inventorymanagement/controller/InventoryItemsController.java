package com.example.quizapp.inventorymanagement.controller;


import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.StockAdjustmentRequest;
import com.example.quizapp.inventorymanagement.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventoryItems")
public class InventoryItemsController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("add")
    public ResponseEntity<String> addItems(@RequestBody List<InventoryItems> inventoryItem){
        inventoryService.addInventoryItems(inventoryItem);
        return new ResponseEntity<>("Items saved", HttpStatus.OK);

    }

    @GetMapping("get")
    public ResponseEntity<List<InventoryItems>> getItems(){
        List<InventoryItems> inventoryItemsList = inventoryService.getInventoryItems().getBody();
        return new ResponseEntity<>(inventoryItemsList, HttpStatus.OK);

    }

    @PutMapping("update/{id}")
    public ResponseEntity<InventoryItems> updateItem(@PathVariable int id ,@RequestBody InventoryItems inventoryItems){
       return inventoryService.updateItem(id,inventoryItems);

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable long id){
        return inventoryService.deleteItem(id);
    }

    @GetMapping("getLowStock")
    public ResponseEntity<List<InventoryItems>> getLowStockItems(){
        return inventoryService.getLowStockItems();
    }

    @GetMapping("/search")
    public Page<InventoryItems> searchInventory(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku_code,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supplier_name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return inventoryService.searchItems(name,sku_code,category,supplier_name,page,size);
    }

    @PostMapping("/{id}/adjust")
    public ResponseEntity<String> adjustStock (
            @PathVariable Long id,
            @RequestBody StockAdjustmentRequest request
            ){
        return inventoryService.adjustStock(id,request);
    }

}
