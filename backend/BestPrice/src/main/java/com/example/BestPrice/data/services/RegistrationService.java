package com.example.BestPrice.data.services;

import com.example.BestPrice.EmailValidator;
import com.example.BestPrice.data.AppUserRole;
import com.example.BestPrice.data.dto.RegistrationRequest;

import com.example.BestPrice.data.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;

    @Transactional
    public void register(RegistrationRequest request) {
//        boolean isValidEmail = emailValidator.test(request.getEmail());
//
//        if (!isValidEmail) {
//            throw new IllegalStateException("email not valid");
//        }

        AppUser appUser = new AppUser(
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
        );

        appUserService.signUpUser(appUser);
    }
}