package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.enum_.Status;
import com.example.quizapp.inventorymanagement.model.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox,Long> {


    @Query(value = "SELECT * FROM notification_outbox WHERE status = 'PENDING' ORDER BY created_at ASC LIMIT 100 FOR UPDATE SKIP LOCKED", nativeQuery = true)
    List<OutBox> findPendingEventsForProcessing();
}
