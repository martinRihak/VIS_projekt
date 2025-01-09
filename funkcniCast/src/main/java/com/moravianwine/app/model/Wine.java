package com.moravianwine.app.model;

import java.sql.SQLException;
import java.time.Clock;
import java.util.List;

import com.moravianwine.app.service.ValueHolder.ValueHolder;
import com.moravianwine.app.service.ValueHolder.ValueLoader;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Year;
import lombok.*;


@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wineId;

    @NotNull(message = "Název nesmí být prázdný")
    private String name;
    private String type;

    @Min(value = 2000, message = "Rok musí být 2000 nebo vyšší")
    @Max(value = 2025 , message = "Rok nesmí být větší než aktuální rok")
    private int year;

    @NotNull(message = "Cena nesmí být prázdná")
    @DecimalMin(value = "50.00", message = "Minimální cena je 50.00")
    private double price;
    private String description;
    @Min(value = 0, message = "Počet na skladě nesmí být záporný")
    private int stockQuantity;
    @ManyToOne
    @JoinColumn(name = "winery_id")
    private Winery winery;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;
    @Transient
    private ValueHolder<List<Review>> reviews;
    @Transient
    private List<Review> reviewList; // Lazy Initialization
    @Transient
    private boolean reviewsLoaded = false;

    public Wine(int wineId, String name, String type, int year, double price, String description, int stockQuantity, Region region, Winery winery) {
        this.wineId = wineId;
        this.name = name;
        this.type = type;
        this.year = year;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.region = region;
        this.winery = winery;
    }


    public void addReviewHolder(ValueLoader<List<Review>> reviewLoader){
        this.reviews = new ValueHolder<>(reviewLoader);
    }
    @Override
    public String toString() {
        return "Wine{" +
                "id=" + wineId +
                ", name='" + name + '\'' +
                ", winery='" + winery.getName() + '\'' +
                ", price=" + price +
                '}';
    }
    public List<Review> getReviewsHolder() throws SQLException {
        return reviews.getValue();
    }
}
