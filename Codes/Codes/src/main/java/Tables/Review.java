package Tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @Getter @AllArgsConstructor
@NoArgsConstructor
public class Review {
    private int reviewId;
    private BigDecimal rating;
    private String comment;
    private java.sql.Timestamp createdAt;
    private int userId;
    private int wineId;

    public Review(BigDecimal rating, String comment, int userId, int wineId) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.wineId = wineId;
    }
}
