package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.context.TenantContext;
import com.example.quizapp.inventorymanagement.model.AuthResponse;
import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Tenant;
import com.example.quizapp.inventorymanagement.model.User;
import com.example.quizapp.inventorymanagement.repository.ProductRepository;
import com.example.quizapp.inventorymanagement.repository.TenantRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TenantRepository tenantRepository;

    public ResponseEntity<String>  addProduct(List<Product> products){

        String tenantId = TenantContext.getCurrentTenant();


        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant ID is missing from the request context.");
        }

        Tenant existingTenant = tenantRepository.findById(UUID.fromString(tenantId))
                .orElseThrow(() -> new RuntimeException("Tenant not found!"));

        if (existingTenant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specified tenant does not exist.");
        }

        products.forEach(product -> product.setTenant(existingTenant));

        productRepo.saveAll(products);
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
