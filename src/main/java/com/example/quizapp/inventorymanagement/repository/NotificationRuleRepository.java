package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.model.NotificationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRuleRepository extends JpaRepository<NotificationRule,Long> {

    List<NotificationRule> findByEventType(String eventType);
}
