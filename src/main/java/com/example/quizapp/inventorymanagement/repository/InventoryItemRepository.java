package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.InventoryItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItems,Long> {

    List<InventoryItems> findByActive(boolean active);
}
