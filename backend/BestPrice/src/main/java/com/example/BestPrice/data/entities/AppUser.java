package com.example.BestPrice.data.entities;

import com.example.BestPrice.data.AppUserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser{


    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    @OneToMany(mappedBy = "appUser")
    private List<Favourites> favourites;

    public AppUser(String email, String password, AppUserRole appUserRole) {
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
