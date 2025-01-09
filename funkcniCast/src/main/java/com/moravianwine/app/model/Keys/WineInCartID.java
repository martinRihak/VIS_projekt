package com.moravianwine.app.model.Keys;

import jakarta.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class WineInCartID implements Serializable {
    private int cart; // ID košíku
    private int wine; // ID vína


    public WineInCartID() {}


    public WineInCartID(int cart, int wine) {
        this.cart = cart;
        this.wine = wine;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
