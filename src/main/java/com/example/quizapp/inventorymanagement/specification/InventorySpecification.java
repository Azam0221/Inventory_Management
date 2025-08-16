package com.example.quizapp.inventorymanagement.specification;


import com.example.quizapp.inventorymanagement.model.InventoryItems;
import org.springframework.data.jpa.domain.Specification;

public class InventorySpecification {

    public static Specification<InventoryItems> hasName(String name){
        return ((root, query, cb) ->
                name==null? null: cb.like(cb.lower(root.get("name")) , "%"+name.toLowerCase()+"%" ));
    }

    public static Specification<InventoryItems> hasSku(String skuCode){
        return ((root, query, cb) ->
                skuCode==null? null: cb.equal(root.get("skuCode") , skuCode));
    }

    public static Specification<InventoryItems> hasCategory(String category){
        return ((root, query, cb) ->
                category==null? null: cb.like(cb.lower(root.get("category")) , "%" +category.toLowerCase()));
    }

    public static Specification<InventoryItems> hasSupplier(String supplierName){
        return ((root, query, cb) ->
                supplierName==null? null: cb.like(cb.lower(root.get("supplierName")) , "%"+supplierName.toLowerCase()));
    }

}
