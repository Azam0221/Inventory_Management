package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.model.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox,Long> {

    List<OutBox> findTop100ByStatusOrderByCreatedAtAsc(Status status);
}
