package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.User;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    InventoryItemRepository inventoryItemRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;


    public void addInventoryItems(List<InventoryItems> inventoryItemsList){
        inventoryItemRepo.saveAll(inventoryItemsList);

        User currentUser = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());

        for(InventoryItems items : inventoryItemsList){
            String sku_code = items.getSku_code();
            String category = items.getCategory();
            transactionService.makeTransaction(
                    items,
                    Type.ADD,
                    items.getQuantity(),
                    currentUser,
                    "Inventory Item ADDED sku_code " + sku_code +
                            " category " + category +
                            " by user " + currentUser.getName()
            );
        }

        System.out.println("Success");
    }

    public ResponseEntity<List<InventoryItems>> getInventoryItems(){
        try {
            return  new ResponseEntity<>(inventoryItemRepo.findByActive(true), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        } return  new ResponseEntity<>( new ArrayList<InventoryItems>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<InventoryItems> updateItem(long id,InventoryItems inventoryItems){
        try{

            InventoryItems existingItem = inventoryItemRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
            existingItem.setId(inventoryItems.getId());
            existingItem.setName(inventoryItems.getName());
            existingItem.setPrice(inventoryItems.getPrice());
            existingItem.setQuantity(inventoryItems.getQuantity());
            existingItem.setCategory(inventoryItems.getCategory());
            existingItem.setDescription(inventoryItems.getDescription());
            existingItem.setSku_code(inventoryItems.getSku_code());
            existingItem.setLowStockThreshold(inventoryItems.getLowStockThreshold());
            existingItem.setSupplierName(inventoryItems.getSupplierName());
            existingItem.setSupplierContact(inventoryItems.getSupplierContact());

            User currentUser = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

                String sku_code = inventoryItems.getSku_code();
                String category = inventoryItems.getCategory();
                transactionService.makeTransaction(
                        inventoryItems,
                        Type.ADD,
                        inventoryItems.getQuantity(),
                        currentUser,
                        "Inventory Item UPDATED sku_code " + sku_code +
                                " category " + category +
                                " by user " + currentUser.getName()
                );
            return new ResponseEntity<>(inventoryItemRepo.save(existingItem),HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  new ResponseEntity<InventoryItems>(new InventoryItems(), HttpStatus.BAD_REQUEST);

    }


    public ResponseEntity<String> deleteItem(long id) {
        try {
            InventoryItems item = inventoryItemRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

            item.setActive(false);
            User currentUser = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()
            );
            String sku_code = item.getSku_code();
            String category = item.getCategory();
            transactionService.makeTransaction(
                    item,
                    Type.REMOVE,
                    -item.getQuantity(),
                    currentUser,
                    "Inventory Item INACTIVE sku_code " + sku_code +
                            " category " + category +
                            " by user " + currentUser.getName()
            );

            return  new ResponseEntity<>("Item deleted ", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
       return  new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<InventoryItems>> getLowStockItems(){
        try {
            return  new ResponseEntity<>(inventoryItemRepo.findLowStockItems(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        } return  new ResponseEntity<>( new ArrayList<InventoryItems>(), HttpStatus.BAD_REQUEST);
    }

}
