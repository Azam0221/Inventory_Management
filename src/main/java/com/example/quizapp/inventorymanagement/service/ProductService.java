package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    public ResponseEntity<String>  addProduct(List<Product> product){
        productRepo.saveAll(product);
        return ResponseEntity.ok("Product added");
    }

    public ResponseEntity<String>  updateProduct(Long id ,Product product){
        try {
            Product product1 = productRepo.findById(id).get();

                    product1.setName(product.getName());
                    product1.setCategory(product.getCategory());
                    product1.setCode(product.getCode());
                    product1.setDescription(product.getDescription());

                    productRepo.save(product1);
                    return ResponseEntity.ok("Product updated");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Product update failed ");
        }
    }

    public ResponseEntity<List<Product>>  getProduct(){
        List<Product> products = productRepo.findAll();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<String>  deleteProduct(Long id){
        productRepo.deleteById(id);
        return ResponseEntity.ok("Product deleted");
    }

}
