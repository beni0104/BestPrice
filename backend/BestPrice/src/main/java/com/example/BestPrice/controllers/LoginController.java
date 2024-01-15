package com.example.BestPrice.controllers;

import com.example.BestPrice.data.SignedInUser;
import com.example.BestPrice.data.dto.RegistrationRequest;
import com.example.BestPrice.data.services.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://192.168.0.147:5500/")
@RequestMapping(path = "/api/v1")
@AllArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SignedInUser signedInUser = SignedInUser.getInstance();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegistrationRequest registrationRequest){
        String response = loginService.checkUser(registrationRequest);
        if (Objects.equals(response, "Credentials are correct!")){
            signedInUser.setUsername(registrationRequest.getEmail());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(response);
    }
}
