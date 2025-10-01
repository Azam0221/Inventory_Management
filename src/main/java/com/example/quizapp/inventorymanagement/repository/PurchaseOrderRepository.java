package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.PurchaseOrder;
import com.example.quizapp.inventorymanagement.service.InventoryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {


}
