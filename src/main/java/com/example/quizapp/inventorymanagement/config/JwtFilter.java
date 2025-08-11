package com.example.quizapp.inventorymanagement.config;



import com.example.quizapp.inventorymanagement.model.AuthResponse;
import com.example.quizapp.inventorymanagement.service.JwtService;
import com.example.quizapp.inventorymanagement.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;
    @Autowired
    private View error;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        System.out.println("Do Filter for path: " + request.getServletPath());
        String path = request.getServletPath();
        if(path.contains("/api/auth/staff/register")||
                path.contains("/api/auth/viewer/login")||
                path.contains("/api/auth/staff/login") ||
                path.contains("/api/auth/admin/login")){
            filterChain.doFilter(request,response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token="";
        String email = "";

        System.out.println("Checking for Authorization header");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            System.out.println("Checking for auth passed");
            token = authHeader.substring(7);
            try {
                email = jwtService.extractUserName(token);
                System.out.println("EMAIL extracted: " + email);
            } catch (Exception e) {
                System.out.println("Error extracting email from token: " + e.getMessage());
                sendError(response, "Invalid token format", HttpStatus.UNAUTHORIZED);
                return;
            }
        }

        else {
            System.out.println("No Authorization header or invalid format");
            sendError(response, "Authorization header missing or invalid", HttpStatus.UNAUTHORIZED);
            return;
        }


        if(email!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);


                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authtoken);
                } else {
                    System.out.println("Token validation failed for user: " + email);
                    sendError(response, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error during authentication: " + e.getMessage());
                sendError(response, "Authentication failed", HttpStatus.UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request,response);


    }

    private void sendError(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        AuthResponse error = new AuthResponse(
                status.value(),
                message,
                null,
                null,
                Map.of("token", message),
                LocalDateTime.now()
        );

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getWriter(), error);

    }
}
