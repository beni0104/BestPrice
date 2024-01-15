package com.example.BestPrice.data.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Favourites {

    @SequenceGenerator(name = "favourite_sequence", sequenceName = "favourite_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favourite_sequence")
    @JsonIgnore
    private Long id;
    private String name;
    private String price;
    private String link;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "appuser_id", referencedColumnName = "id")
    private AppUser appUser;

    public Favourites(String name, String price, String link) {
        this.name = name;
        this.price = price;
        this.link = link;
    }
}
