package com.moravianwine.app.model;


import com.moravianwine.app.model.Keys.WineInCartID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wine_in_cart")
public class WineInCart {

    @EmbeddedId
    private WineInCartID id;

    @ManyToOne
    @MapsId("cart")
    @JoinColumn(name = "cart_id", nullable = false) // FK na Cart
    private Cart cart;
    @ManyToOne
    @MapsId("wine")
    @JoinColumn(name = "wine_id", nullable = false) // FK na Wine
    private Wine wine;

    private int quantity; // Počet položek

    public WineInCart(Cart cart, Wine wine, int quantity) {
        this.id = new WineInCartID(cart.getCartId(), wine.getWineId());
        this.cart = cart;
        this.wine = wine;
        this.quantity = quantity;
    }
}


