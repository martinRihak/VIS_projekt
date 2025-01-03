package DataSources.table;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WineTableGateway {
    private Connection connection;
    public WineTableGateway(Connection connection) {
        this.connection = connection;
    }

    public void insertWine(String name, String type, int year, double price, String description, int stockQuantity, int wineryId, int regionId) throws SQLException {
        String sql = "INSERT INTO wine (name, type, year, price, description, stock_quantity, winery_id, region_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, year);
            statement.setDouble(4, price);
            statement.setString(5, description);
            statement.setInt(6, stockQuantity);
            statement.setInt(7, wineryId);
            statement.setInt(8, regionId);
            statement.executeUpdate();
        }
    }

    public void updateWine(int wineId, String name, String type, int year, double price, String description, int stockQuantity, int wineryId, int regionId) throws SQLException {
        String sql = "UPDATE wine SET name = ?, type = ?, year = ?, price = ?, description = ?, stock_quantity = ?, winery_id = ?, region_id = ? WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, year);
            statement.setDouble(4, price);
            statement.setString(5, description);
            statement.setInt(6, stockQuantity);
            statement.setInt(7, wineryId);
            statement.setInt(8, regionId);
            statement.setInt(9, wineId);
            statement.executeUpdate();
        }
    }

    public void deleteWine(int wineId) throws SQLException {
        String sql = "DELETE FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            statement.executeUpdate();
        }
    }

    public List<String> getAllWines() throws SQLException {
        List<String> wines = new ArrayList<>();
        String sql = "SELECT name, type, year, price, description, stock_quantity FROM wine";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String wineDetails = String.format("Name: %s, Type: %s, Year: %d, Price: %.2f, Description: %s, Stock: %d",
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getInt("year"),
                        resultSet.getDouble("price"),
                        resultSet.getString("description"),
                        resultSet.getInt("stock_quantity"));
                wines.add(wineDetails);
            }
        }
        return wines;
    }
    public int getStockQuantity(int wineId) throws SQLException {
        String sql = "SELECT stock_quantity FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
            return -1;
        }
    }
    public BigDecimal getPrice(int wineId) throws SQLException {
        String sql = "SELECT price FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal(1);
                }
            }
            return null;
        }
    }
}