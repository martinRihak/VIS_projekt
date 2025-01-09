package com.moravianwine.app.model;


import com.moravianwine.app.model.Keys.FavoriteWineID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorite_wines")
public class FavoriteWines {
    @EmbeddedId
    private FavoriteWineID id;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id" ,nullable = false)
    private Users user;

    @ManyToOne
    @MapsId("wine")
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;


    private Timestamp addedAt;

    public FavoriteWines(Users user, Wine wine) {
        this.id = new FavoriteWineID(user,wine);
        this.user = user;
        this.wine = wine;
    }
}
