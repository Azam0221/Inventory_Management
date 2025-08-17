package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.InventoryItems;
import com.example.quizapp.inventorymanagement.model.StockAdjustmentRequest;
import com.example.quizapp.inventorymanagement.model.User;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import com.example.quizapp.inventorymanagement.specification.InventorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
            InventoryItems item = inventoryItemRepo.findById(id).orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
            item.setActive(false);
            User currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            String sku_code = item.getSku_code();
            String category = item.getCategory();
            transactionService.makeTransaction(
                    item,
                    Type.REMOVE,
                    -item.getQuantity(),
                    currentUser,
                    "Inventory Item INACTIVE sku_code " + sku_code +
                            " category " + category +
                            " by user " + currentUser.getName());
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

    public Page<InventoryItems> searchItems(String name, String sku_code,String category,String supplier_name,int page, int size){
        Specification<InventoryItems>  spec = Specification
                .allOf(InventorySpecification.hasName(name),
                InventorySpecification.hasCategory(category),
                InventorySpecification.hasSku(sku_code),
                InventorySpecification.hasSupplier(supplier_name));

        return inventoryItemRepo.findAll(spec, PageRequest.of(page,size));
    }

    public ResponseEntity<String> adjustStock(Long id,StockAdjustmentRequest request){

        InventoryItems inventoryItems = inventoryItemRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        int newQuantity = request.getAdjustment()+inventoryItems.getQuantity();

        if(newQuantity<0){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid adjustment");
        }

        User currentUser = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        inventoryItems.setQuantity(newQuantity);
        transactionService.makeTransaction(
                inventoryItems,
                Type.ADJUSTMENT,
                request.getAdjustment(),
                currentUser,
                "Inventory Item ADJUSTMENT : Why? " +  request.getRemarks() +   " sku_code: " + inventoryItems.getSku_code() +
                        " category: " + inventoryItems.getCategory() +
                        " by user: " + currentUser.getName());
        inventoryItemRepo.save(inventoryItems);

        return ResponseEntity.ok("Stock adjusted successfully");

    }

}
