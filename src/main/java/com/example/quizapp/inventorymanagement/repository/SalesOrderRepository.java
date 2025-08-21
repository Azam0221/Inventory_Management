package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder,Long> {

}
