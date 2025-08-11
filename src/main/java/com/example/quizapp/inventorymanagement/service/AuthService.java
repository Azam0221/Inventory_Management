package com.example.quizapp.inventorymanagement.service;



import com.example.quizapp.inventorymanagement.model.*;
import com.example.quizapp.inventorymanagement.repository.TokenRepository;
import com.example.quizapp.inventorymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    TokenRepository tokenRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public ResponseEntity<AuthResponse> register(RegisterRequest request){
        String accessToken = "";
        String refreshToken = "";

        if(userRepo.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(400, "Email already exists", null, null,
                            Map.of("email", "This email is already registered"), LocalDateTime.now()));
        }



        else {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRole(request.getRole());

        user.setPassword(encoder.encode(user.getPassword()));


        userRepo.save(user);
         accessToken = jwtService.generateAcesssToken(request.getEmail(),request.getRole());
         refreshToken = jwtService.generateRefreshToken(request.getEmail(),request.getRole());
        saveUserToken(user,refreshToken);
        }

        return ResponseEntity.ok(new AuthResponse(200, "User registered successfully",accessToken , refreshToken, Map.of(), LocalDateTime.now()));
    }


    public ResponseEntity<AuthResponse> loginVerify(LoginRequest loginRequest){
        if(loginRequest.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new AuthResponse(400, "Email not found", null, null,
                            Map.of("email", "Email is empty"), LocalDateTime.now()));
        }
        User user = userRepo.findByEmail(loginRequest.getEmail());



        String accessToken ="";
        String refreshToken ="";

        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

        if(authentication.isAuthenticated()){

            accessToken = jwtService.generateAcesssToken(loginRequest.getEmail(),user.getRole());
            refreshToken = jwtService.generateRefreshToken(loginRequest.getEmail(),user.getRole());

            return  ResponseEntity.ok(new AuthResponse(
                    200,
                    "User login successfully",
                    accessToken,
                    refreshToken,
                    Map.of(),
                    LocalDateTime.now()
            ));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new AuthResponse(400, "Login failed", null, null,
                        Map.of("email", "Something went wrong try again"), LocalDateTime.now()));
    }
    
    public void saveUserToken(User user,String token){
        Token saveToken = new Token();
        saveToken.setUser(user);
        saveToken.setToken(token);
        saveToken.setExpired(false);
        saveToken.setRevoked(false);

        tokenRepository.save(saveToken);
    }



    private void revokeAllToken(User user){
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user);
        validTokens.forEach(token -> {
                    token.setRevoked(true);
                    token.setRevoked(true);
        }
        );

        tokenRepository.saveAll(validTokens);

    }

}
