package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox,Long> {

}
