package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItems,Long> , JpaSpecificationExecutor<InventoryItems> {

    List<InventoryItems> findByActive(boolean active);

    @Query(value = "SELECT * FROM inventory_items i WHERE i.quantity<=i.low_Stock_Threshold ",nativeQuery = true)
    List<InventoryItems> findLowStockItems();

    InventoryItems findByProductAndSupplier(Product product, Supplier supplier);

    InventoryItems findByProduct(Product product);
}
