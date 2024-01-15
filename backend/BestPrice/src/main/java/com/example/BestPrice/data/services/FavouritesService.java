package com.example.BestPrice.data.services;


import com.example.BestPrice.data.SignedInUser;
import com.example.BestPrice.data.entities.AppUser;
import com.example.BestPrice.data.entities.Favourites;
import com.example.BestPrice.data.repositories.AppUserRepository;
import com.example.BestPrice.data.repositories.FavouritesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FavouritesService {

    private final FavouritesRepository favouritesRepository;
    private final SignedInUser signedInUser = SignedInUser.getInstance();
    private final AppUserRepository appUserRepository;


    public void saveFavourite(Favourites favourites){
        Optional<AppUser> user = appUserRepository.findByEmail(signedInUser.getUsername());
        if(user.isPresent()){
            favourites.setAppUser(user.get());
            favouritesRepository.save(favourites);
        }

    }

    public void deleteFavouriteByLink(String link){
        favouritesRepository.deleteByLink(link);
    }

    public List<Favourites> getAllFavourites(){
        Optional<AppUser> user = appUserRepository.findByEmail(signedInUser.getUsername());
        return user.map(appUser -> favouritesRepository.findByAppUserId(appUser.getId())).orElse(null);
    }

}
