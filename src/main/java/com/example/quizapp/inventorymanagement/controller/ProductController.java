package com.example.quizapp.inventorymanagement.controller;

import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody List<Product> productList){
        return productService.addProduct(productList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id , @RequestBody Product product){
        return productService.updateProduct(id, product);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProduct(){
        return productService.getProduct();
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        return productService.deleteProduct(id);
    }

}
