package com.example.BestPrice.controllers;


import com.example.BestPrice.data.dto.FavouriteDTO;
import com.example.BestPrice.data.entities.Favourites;
import com.example.BestPrice.data.services.FavouritesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://192.168.0.147:5500/")
@RequestMapping("/favourites")
@AllArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;

    @GetMapping("/getall")
    public List<Favourites> getAllFavourites(){
        return favouritesService.getAllFavourites();
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFavourite(@RequestBody FavouriteDTO favouriteDTO){
        Favourites favourites = new Favourites(
                favouriteDTO.getName(),
                favouriteDTO.getPrice(),
                favouriteDTO.getLink()
        );

        favouritesService.saveFavourite(favourites);

        return ResponseEntity.ok("Saved");
    }
}
