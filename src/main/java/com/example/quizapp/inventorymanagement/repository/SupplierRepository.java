package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

}
