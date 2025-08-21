package com.example.quizapp.inventorymanagement.repository;

import com.example.quizapp.inventorymanagement.enum_.TargetType;
import com.example.quizapp.inventorymanagement.model.Responsibility;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResponsibilityRepository extends JpaRepository<Responsibility,Long> {

    Optional<Responsibility> findByName(String name);
}
