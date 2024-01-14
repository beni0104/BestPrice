package com.example.BestPrice.controllers;

import com.example.BestPrice.data.dto.RegistrationRequest;
import com.example.BestPrice.data.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://192.168.0.147:5500/")
@RequestMapping(path = "/register")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok("User registered successfully!");
    }
}
