package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Supplier;
import com.example.quizapp.inventorymanagement.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {


    @Autowired
    SupplierRepository supplierRepo;

    public ResponseEntity<String> addSupplier(List<Supplier> suppliers){
        supplierRepo.saveAll(suppliers);
        return ResponseEntity.ok("Suppliers got  added");
    }

    public ResponseEntity<String>  updateSupplier(Long id , Supplier supplier){
        try {
            Supplier supplier1 = supplierRepo.findById(id).get();

            supplier1.setName(supplier.getName());
            supplier1.setContact(supplier.getContact());
            supplier1.setCode(supplier.getCode());


            supplierRepo.save(supplier1);
            return ResponseEntity.ok("Supplier updated");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Supplier entry  update failed ");
        }
    }

    public ResponseEntity<List<Supplier>>  getSupplier(){
        List<Supplier> suppliers = supplierRepo.findAll();

        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    public ResponseEntity<String>  deleteSupplier(Long id){
        supplierRepo.deleteById(id);
        return ResponseEntity.ok("Supplier deleted");
    }

}
