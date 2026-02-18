package com.example.quizapp.inventorymanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomController {

    @GetMapping("/custom")
    public String welcome() {
        return "Welcome to the Inventory Management System!";
    }
}
