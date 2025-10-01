package com.example.quizapp.inventorymanagement;

import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.Product;
import com.example.quizapp.inventorymanagement.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class StaticInventoryData {

    public static List<InventoryItems> getLowStockItems() {
        List<InventoryItems> lowStockList = new ArrayList<>();

        // Example Products
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setId(2);
        product2.setName("Product B");

        // Example Suppliers
        Supplier supplier1 = new Supplier();
        supplier1.setId(1);
        supplier1.setName("Supplier X");

        Supplier supplier2 = new Supplier();
        supplier2.setId(2);
        supplier2.setName("Supplier Y");

        // Low-stock Inventory Items
        InventoryItems item1 = new InventoryItems();
        item1.setId(1);
        item1.setSku_code("SKU001");
        item1.setQuantity(5);  // low stock
        item1.setPrice(50.0);
        item1.setLowStockThreshold(10);
        item1.setActive(true);
        item1.setProduct(product1);
        item1.setSupplier(supplier1);

        InventoryItems item2 = new InventoryItems();
        item2.setId(2);
        item2.setSku_code("SKU002");
        item2.setQuantity(3);  // low stock
        item2.setPrice(30.0);
        item2.setLowStockThreshold(15);
        item2.setActive(true);
        item2.setProduct(product2);
        item2.setSupplier(supplier2);

        lowStockList.add(item1);
        lowStockList.add(item2);

        return lowStockList;
    }
}
