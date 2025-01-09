package com.moravianwine.app.repository;

import com.moravianwine.app.model.Review;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.service.ValueHolder.ValueLoader;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ReviewgateWay {
    private Connection connection;
    public ReviewgateWay(Connection connection) {
        this.connection = connection;
    }

    public boolean hasReview(int userId, int wineId) throws SQLException {
        String sql = "SELECT 1 FROM review WHERE user_id = ? AND wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Vrací true, pokud existuje alespoň jeden záznam
            }
        }
    }

    // Vloží novou recenzi
    public void insertReview(BigDecimal rating, String comment, int user_id, int wine_id) throws SQLException {
        String sql = "INSERT INTO review (rating, comment, user_id, wine_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, rating);
            statement.setString(2, comment);
            statement.setInt(3, user_id);
            statement.setInt(4, wine_id);
            statement.executeUpdate();
        }
    }
    public boolean hasReview(Connection connection, Users user, Wine wine) throws SQLException {
        String sql = "SELECT 1 FROM review WHERE user_id = ? AND wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getUserId());
            statement.setInt(2, wine.getWineId());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insertReview(Review review) throws SQLException {
        String sql = "INSERT INTO review (rating, comment, user_id, wine_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1,review.getRating());
            statement.setString(2, review.getComment());
            statement.setInt(3,review.getUserId().getUserId());
            statement.setInt(4,review.getWineId().getWineId());
            statement.executeUpdate();
        }
    }

    public ValueLoader<List<Review>> createReviewLoader(Wine wine) {
        return () -> {
            List<Review> reviews = new ArrayList<>();

            String sql = "SELECT * FROM review WHERE wine_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, wine.getWineId());
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Users user = new UserMapper(connection).findById(resultSet.getInt("user_id"));
                        Review review = new Review(
                                resultSet.getInt("review_id"),
                                wine,
                                user,
                                resultSet.getBigDecimal("rating"),
                                resultSet.getString("comment")
                        );
                        reviews.add(review);
                    }
                }
            }
            return reviews;
        };
    }
    public int getLastReviewId() throws SQLException {
        String sql = "SELECT id FROM reviews ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new RuntimeException("No reviews found in the database.");
            }
        }
    }
}
