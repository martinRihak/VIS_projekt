package com.moravianwine.app.model.Keys;

import com.moravianwine.app.model.Users;
import com.moravianwine.app.model.Wine;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter
public class FavoriteWineID implements Serializable {
    private int user;
    private int wine;

    public FavoriteWineID() {}

    public FavoriteWineID(Users user, Wine wine) {
        this.user = user.getUserId();
        this.wine = wine.getWineId();
    }
    public FavoriteWineID(int user, int wine) {
        this.user = user;
        this.wine = wine;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
