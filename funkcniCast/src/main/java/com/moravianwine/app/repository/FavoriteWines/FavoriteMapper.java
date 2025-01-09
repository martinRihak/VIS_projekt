package com.moravianwine.app.repository.FavoriteWines;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class FavoriteMapper {
    private final Connection connection;

    public FavoriteMapper(Connection connection) {
        this.connection = connection;
    }

    public void addFavorite(int userId, int wineId) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM favorite_wines WHERE user_id = ? AND wine_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, wineId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                this.removeFavorite(userId, wineId);
                throw new IllegalArgumentException("Odstranuji Vino z Oblibenych");

            }
        }

        String insertQuery = "INSERT INTO favorite_wines (user_id, wine_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, wineId);
            stmt.executeUpdate();
        }
    }

    public void removeFavorite(int userId, int wineId) throws SQLException {
        String query = "DELETE FROM favorite_wines WHERE user_id = ? AND wine_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, wineId);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getFavoritesByUser(int userId) throws SQLException {
        String query = "SELECT w.wine_id FROM favorite_wines w WHERE w.user_id = ?";
        List<Integer> favorites = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favorites.add(rs.getInt(1));
            }
        }
        return favorites;
    }
}
