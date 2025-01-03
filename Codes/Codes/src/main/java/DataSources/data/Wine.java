package DataSources.data;
import ORB_folder.ValueHolder;
import ORB_folder.ValueLoader;
import Tables.Review;
import Tables.Winery;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
public class Wine {
    private int wineId;
    private String name;
    private String type;
    private int year;
    private double price;
    private String description;
    private int stockQuantity;
    private Winery winery;
    private int regionId;
    private ValueHolder<List<Review>> reviews;
    private List<Review> reviewList; // Lazy Initialization
    private boolean reviewsLoaded = false;
    public Wine() {}
    public Wine(int wineId, String name, String type, int year, double price,
                String description, int stockQuantity, Winery winery, int regionId
                ) {
        this.wineId = wineId;
        this.name = name;
        this.type = type;
        this.year = year;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.winery = winery;
        this.regionId = regionId;
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
    public List<Review> getReviews() throws SQLException {
        return reviews.getValue();
    }
}