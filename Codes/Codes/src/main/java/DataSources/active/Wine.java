package DataSources.active;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Getter @Setter
public class Wine {
    private int wineId;
    private String name;
    private String type;
    private int year;
    private double price;
    private String description;
    private int stockQuantity;
    private int wineryId;
    private int regionId;
    public Wine(){}
    public Wine(int wineId,String name, String type, int year, double price,
                String description, int stockQuantity, int wineryId, int regionId) {
        this.wineId = wineId;
        this.name = name;
        this.type = type;
        this.year = year;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.wineryId = wineryId;
        this.regionId = regionId;
    }

    public void insert(Connection connection) throws SQLException {
        String sql = "INSERT INTO wine (name, type, year, price, description, stock_quantity, winery_id, region_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

    public void update(Connection connection) throws SQLException {
        String sql = "UPDATE wine SET name = ?, type = ?, year = ?, price = ?, description = ?, " +
                "stock_quantity = ?, winery_id = ?, region_id = ? WHERE wine_id = ?";
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

    public static Wine findById(Connection connection, int wineId) throws SQLException {
        String sql = "SELECT * FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Wine(wineId,
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getInt("year"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getInt("stock_quantity"),
                            resultSet.getInt("winery_id"),
                            resultSet.getInt("region_id"));
                }
            }
        }
        return null;
    }
    public void delete(Connection connection) throws SQLException {
        String sql = "DELETE FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            statement.executeUpdate();
        }
    }
}
/**
 *
 *
 *
 */