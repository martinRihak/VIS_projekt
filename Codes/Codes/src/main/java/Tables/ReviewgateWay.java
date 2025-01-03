package Tables;


import ORB_folder.ValueLoader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewgateWay {

    public boolean hasReview(Connection connection,int userId, int wineId) throws SQLException {
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
    public void insertReview(Connection connection, BigDecimal rating, String comment,int user_id,int wine_id) throws SQLException {
        String sql = "INSERT INTO review (rating, comment, user_id, wine_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, rating);
            statement.setString(2, comment);
            statement.setInt(3, user_id);
            statement.setInt(4, wine_id);
            statement.executeUpdate();
        }
    }
    public boolean hasReview(Connection connection,Users user, Wine wine) throws SQLException {
        String sql = "SELECT 1 FROM review WHERE user_id = ? AND wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user.getUserId());
            statement.setInt(2, wine.getWineId());
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insertReview(Connection connection,Review review) throws SQLException {
        String sql = "INSERT INTO review (rating, comment, user_id, wine_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1,review.getRating());
            statement.setString(2, review.getComment());
            statement.setInt(3,review.getUserId());
            statement.setInt(4,review.getWineId());
            statement.executeUpdate();
        }
    }

    public List<Review> findReviewsByWineId(Connection connection,int wineId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reviews.add(new Review(
                            resultSet.getBigDecimal("rating"),
                            resultSet.getString("comment"),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("wine_id")
                    ));
                }
            }
        }
        return reviews;
    }
    public ValueLoader<List<Review>> createReviewLoader(Connection connection, int wineId) {
        return () -> {
            List<Review> reviews = new ArrayList<>();
            String sql = "SELECT * FROM review WHERE wine_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, wineId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        reviews.add(new Review(
                                resultSet.getBigDecimal("rating"),
                                resultSet.getString("comment"),
                                resultSet.getInt("user_id"),
                                resultSet.getInt("wine_id")
                        ));
                    }
                }
            }
            return reviews;
        };
    }

}
