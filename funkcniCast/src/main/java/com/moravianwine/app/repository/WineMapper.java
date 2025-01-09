package com.moravianwine.app.repository;


import com.moravianwine.app.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class WineMapper {
    private Connection connection;
    private WineryMapper wineryMapper;
    public WineMapper(Connection connection) {
        this.connection = connection;
        this.wineryMapper = new WineryMapper(connection);
    }

    public void insert(Wine wine) throws SQLException {
        String sql = "INSERT INTO wine (name, type, year, price, description, stock_quantity, winery_id, region_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            insertWine(wine, statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void insertWine(Wine wine, PreparedStatement statement) throws SQLException {
        statement.setString(1, wine.getName());
        statement.setString(2, wine.getType());
        statement.setInt(3, wine.getYear());
        statement.setBigDecimal(4, BigDecimal.valueOf(wine.getPrice()));
        statement.setString(5, wine.getDescription());
        statement.setInt(6, wine.getStockQuantity());
        statement.setInt(7, wine.getWinery().getWineryId());
        statement.setInt(8, wine.getRegion().getRegionId());
    }

    public void update(Wine wine) throws SQLException {
        String sql = "UPDATE wine SET name = ?, type = ?, year = ?, price = ?, description = ?, stock_quantity = ?, winery_id = ?, region_id = ? WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            insertWine(wine, statement);
            statement.setInt(7, wine.getWinery().getWineryId());
            statement.setInt(9, wine.getWineId());
            statement.executeUpdate();
        }
    }
    public void updatePriceFromCart(Cart cart) throws SQLException {
        String sql = "UPDATE wine SET stock_quantity = stock_quantity - ? WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (WineInCart wine : cart.getWinesInCart().values()){
                statement.setInt(1,wine.getQuantity());
                statement.setInt(2,wine.getWine().getWineId());
                statement.executeUpdate();
            }
        }
    }
    public void delete(Wine wine) throws SQLException {
        String sql = "DELETE FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wine.getWineId());
            statement.executeUpdate();
        }
    }

    public Wine findById(int wineId) throws SQLException {
        String sql = "SELECT * FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToWine(resultSet);
                }
            }
        }
        return null;
    }

    public Map<Integer, Wine> findAll() throws SQLException {
        Map<Integer, Wine> wineMap = new HashMap<>();
        String sql = "SELECT * FROM wine";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Wine wine = mapRowToWine(resultSet);
                wineMap.put(wine.getWineId(), wine);
            }
        }
        System.out.println("Vstup do databaze");
        return wineMap;
    }

    private Wine mapRowToWine(ResultSet resultSet) throws SQLException {
        int wineryId = resultSet.getInt("winery_id");
        int regionId = resultSet.getInt("region_id");
        Winery winery = wineryMapper.findById(wineryId); // Načtení vinařství podle wineryId
        Region region = new RegionGateWay(connection).findById(regionId);
        return new Wine(
                resultSet.getInt("wine_id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                resultSet.getInt("year"),
                resultSet.getDouble("price"),
                resultSet.getString("description"),
                resultSet.getInt("stock_quantity"),
                region, winery
        );
    }
    public boolean updateStockQuantity(int wineId, int newQuantity) throws SQLException {
        String sql = "UPDATE wine SET stock_quantity = ? WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, newQuantity);
            statement.setInt(2, wineId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
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
}
