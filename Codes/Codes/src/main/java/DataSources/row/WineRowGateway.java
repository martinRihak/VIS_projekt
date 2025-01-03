package DataSources.row;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter @Setter
public class WineRowGateway {
    private int wineId;
    private String name;
    private String type;
    private int year;
    private BigDecimal price;
    private String description;
    private int stockQuantity;
    private int wineryId;
    private int regionId;
    public WineRowGateway(Connection connection,int wineId) throws SQLException {
        findById(connection,wineId);
    }
    public void insert(Connection connection) throws SQLException {
        String sql = "INSERT INTO wine (name, type, year, price, description, stock_quantity, winery, region_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, type);
            statement.setInt(3, year);
            statement.setBigDecimal(4, price);
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
            statement.setBigDecimal(4, price);
            statement.setString(5, description);
            statement.setInt(6, stockQuantity);
            statement.setInt(7, wineryId);
            statement.setInt(8, regionId);
            statement.setInt(9, wineId);
            statement.executeUpdate();
        }
    }

    public void delete(Connection connection) throws SQLException {
        String sql = "DELETE FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineId);
            statement.executeUpdate();
        }
    }
    private void findById(Connection connection,int id) throws SQLException {
        String sql = "SELECT * FROM wine WHERE wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    this.wineId = resultSet.getInt("wine_id");
                    this.name = resultSet.getString("name");
                    this.type = resultSet.getString("type");
                    this.year = resultSet.getInt("year");
                    this.price = resultSet.getBigDecimal("price");
                    this.description = resultSet.getString("description");
                    this.stockQuantity = resultSet.getInt("stock_quantity");
                    this.wineryId = resultSet.getInt("winery_id");
                    this.regionId = resultSet.getInt("region_id");
                }
            }
        }
    }
}





