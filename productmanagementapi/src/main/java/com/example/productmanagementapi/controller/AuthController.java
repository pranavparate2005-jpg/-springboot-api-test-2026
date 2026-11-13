package com.example.productmanagementapi.controller;


import com.example.productmanagementapi.dto.LoginRequestDTO;
import com.example.productmanagementapi.dto.LoginResponseDTO;
import com.example.productmanagementapi.dto.UserRequestDTO;
import com.example.productmanagementapi.dto.UserResponseDTO;
import com.example.productmanagementapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO dto){

        UserResponseDTO response = authService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){

        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(response);
    }
}
