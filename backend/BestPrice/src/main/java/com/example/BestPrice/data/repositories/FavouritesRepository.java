package com.example.BestPrice.data.repositories;

import com.example.BestPrice.data.entities.Favourites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends JpaRepository<Favourites, Long> {

    List<Favourites> findByAppUserId (Long appUserId);
    void deleteByLink(String link);
}
