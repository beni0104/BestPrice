package com.example.BestPrice.data.services;

import com.example.BestPrice.data.AppUserRole;
import com.example.BestPrice.data.dto.RegistrationRequest;
import com.example.BestPrice.data.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final AppUserService appUserService;

    public String checkUser(RegistrationRequest registrationRequest){

        AppUser appUser = new AppUser(
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                AppUserRole.USER
        );

        return appUserService.verifyUser(appUser);
    }
}
