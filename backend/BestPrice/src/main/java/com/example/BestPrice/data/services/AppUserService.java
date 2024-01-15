package com.example.BestPrice.data.services;

import com.example.BestPrice.data.entities.AppUser;
import com.example.BestPrice.data.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class AppUserService{

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public String verifyUser(AppUser appUser){
        Optional<AppUser> user = appUserRepository.findByEmail(appUser.getEmail());
        if (user.isPresent()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (encoder.matches(appUser.getPassword(), user.get().getPassword())){
                return "Credentials are correct!";
            }
            else return "Incorrect password!";
        }
        else return "User with given email does not exist!";
    }

}
