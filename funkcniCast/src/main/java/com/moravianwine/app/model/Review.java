package com.moravianwine.app.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "wine_id")
    private Wine wineId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userId;
    private BigDecimal rating;
    private String comment;

    public Review(Wine wine, Users users, BigDecimal rating, String comment) {
        this.wineId = wine;
        this.userId = users;
        this.rating = rating;
        this.comment = comment;
    }
}

