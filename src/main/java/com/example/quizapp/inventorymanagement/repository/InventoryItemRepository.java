package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.InventoryItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItems,Long> {

}
