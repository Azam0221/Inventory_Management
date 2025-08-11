package com.example.quizapp.inventorymanagement.controller;


import com.example.quizapp.inventorymanagement.enum_.Role;
import com.example.quizapp.inventorymanagement.model.AuthResponse;
import com.example.quizapp.inventorymanagement.model.LoginRequest;
import com.example.quizapp.inventorymanagement.model.RegisterRequest;
import com.example.quizapp.inventorymanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin/register")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody RegisterRequest request){
        request.setRole(Role.ADMIN);
        return authService.register(request);
    }

    @PostMapping("/staff/register" )
    public ResponseEntity<AuthResponse> registerStaff(@RequestBody RegisterRequest request){
        request.setRole(Role.STAFF);
        return authService.register(request);
    }

    @PostMapping("/viewer/register")
    public ResponseEntity<AuthResponse> registerViewer(@RequestBody RegisterRequest request){
        request.setRole(Role.VIEWER);
        return authService.register(request);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> loginAdmin(@RequestBody LoginRequest request){
        return authService.loginVerify(request);
    }

    @PostMapping("/staff/login")
    public ResponseEntity<AuthResponse> loginStaff(@RequestBody LoginRequest request){

        return authService.loginVerify(request);
    }

    @PostMapping("/viewer/login")
    public ResponseEntity<AuthResponse> loginViewer(@RequestBody LoginRequest request){
        return authService.loginVerify(request);
    }


    @GetMapping("/hello")
    public String hello1(){

        return "Hello its working ";
    }
}
