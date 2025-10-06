package com.example.quizapp.inventorymanagement.service;


import com.example.quizapp.inventorymanagement.context.TenantContext;
import com.example.quizapp.inventorymanagement.enum_.Type;
import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.InventoryItemRepository;
import com.example.quizapp.inventorymanagement.repository.ProductRepository;
import com.example.quizapp.inventorymanagement.repository.TenantRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import com.example.quizapp.inventorymanagement.specification.InventorySpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.UUID;

@Service
public class InventoryService {

    @Autowired
    InventoryItemRepository inventoryItemRepo;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    TenantRepository tenantRepository;


    public ResponseEntity<String> addInventoryItems(List<InventoryItems> inventoryItemsList){

        String tenantId = TenantContext.getCurrentTenant();


        if (tenantId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant ID is missing from the request context.");
        }

        Tenant existingTenant = tenantRepository.findById(UUID.fromString(tenantId))
                .orElseThrow(() -> new RuntimeException("Tenant not found!"));

        if (existingTenant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The specified tenant does not exist.");
        }

        inventoryItemsList.forEach(inventoryItems -> inventoryItems.setTenant(existingTenant));

        inventoryItemRepo.saveAll(inventoryItemsList);

        User currentUser = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName());

        for(InventoryItems items : inventoryItemsList){
            String sku_code = items.getSku_code();
            String category = items.getProduct().getCategory();
            Supplier supplier = items.getSupplier();
            transactionService.makeTransaction(
                    items,
                    Type.ADD,
                    items.getQuantity(),
                    currentUser,
                    "Inventory Item ADDED sku_code " + sku_code +
                            " category " + category +
                            " by user " + currentUser.getName(),
                    supplier

            );
        }

        return ResponseEntity.ok("Inventory item added");
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
            existingItem.setPrice(inventoryItems.getPrice());
            existingItem.setQuantity(inventoryItems.getQuantity());
            existingItem.setSku_code(inventoryItems.getSku_code());
            existingItem.setLowStockThreshold(inventoryItems.getLowStockThreshold());

            User currentUser = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName());

                String sku_code = inventoryItems.getSku_code();
                String category = inventoryItems.getProduct().getCategory();
                Supplier supplier = inventoryItems.getSupplier();
                transactionService.makeTransaction(
                        inventoryItems,
                        Type.ADD,
                        inventoryItems.getQuantity(),
                        currentUser,
                        "Inventory Item UPDATED sku_code " + sku_code +
                                " category " + category +
                                " by user " + currentUser.getName(),
                        supplier

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
            String category = item.getProduct().getCategory();
            Supplier supplier = item.getSupplier();
            transactionService.makeTransaction(
                    item,
                    Type.REMOVE,
                    -item.getQuantity(),
                    currentUser,
                    "Inventory Item INACTIVE sku_code " + sku_code +
                            " category " + category +
                            " by user " + currentUser.getName(),
                    supplier);
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
        Supplier supplier = new Supplier();
        inventoryItems.setQuantity(newQuantity);
        transactionService.makeTransaction(
                inventoryItems,
                Type.ADJUSTMENT,
                request.getAdjustment(),
                currentUser,
                "Inventory Item ADJUSTMENT : Why? " +  request.getRemarks() +   " sku_code: " + inventoryItems.getSku_code() +
                        " category: " + inventoryItems.getProduct().getCategory() +
                        " by user: " + currentUser.getName(),
                supplier);
        inventoryItemRepo.save(inventoryItems);

        return ResponseEntity.ok("Stock adjusted successfully");

    }

    public ResponseEntity<String> increaseStock(Product product , int quantity,Supplier supplier){

        InventoryItems item = inventoryItemRepo.findByProductAndSupplier(product,supplier);
        int prevQuantity = item.getQuantity();
        if(item!=null){
            item.setQuantity(prevQuantity +quantity);
            item.setSupplier(supplier);
        }
        else{
            return new ResponseEntity<String>("Item not found",HttpStatus.BAD_REQUEST);
        }

        inventoryItemRepo.save(item);
        return new ResponseEntity<String>(item+"Quantity has been increased",HttpStatus.OK);

    }

    public ResponseEntity<String> decreaseStock(Product product , int quantity)  {

        InventoryItems item = inventoryItemRepo.findByProduct(product);
        int prevQuantity = item.getQuantity();
        if(item!=null){
            item.setQuantity(prevQuantity - quantity);
        }

        else if(prevQuantity < quantity) {
            try {
                notificationService.sendLowStockAlert((List<InventoryItems>) item);
                return new ResponseEntity<>("Insufficient stock", HttpStatus.BAD_REQUEST);
            }
            catch (Exception e){
                throw new RuntimeException("Error in sending low stock alert");
            }
            }


        else{
            return new ResponseEntity<String>("Item not found",HttpStatus.BAD_REQUEST);
        }

        inventoryItemRepo.save(item);
        return new ResponseEntity<String>(item+"Quantity has been increased",HttpStatus.OK);

    }


}
