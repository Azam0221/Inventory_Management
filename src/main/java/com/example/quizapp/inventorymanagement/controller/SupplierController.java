package com.example.quizapp.inventorymanagement.controller;

import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Supplier;
import com.example.quizapp.inventorymanagement.service.ProductService;
import com.example.quizapp.inventorymanagement.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;



    @PostMapping("/add")
    public ResponseEntity<String> addSuppliers(@RequestBody List<Supplier> suppliers){
        return supplierService.addSupplier(suppliers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateSupplier(@PathVariable Long id , @RequestBody Supplier supplier){
        return supplierService.updateSupplier(id,supplier);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Supplier>> getAllSupplier(){
        return  supplierService.getSupplier();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return supplierService.deleteSupplier(id);
    }
}
