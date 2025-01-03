package Tables;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineryMapper {
    private final Connection connection;

    public WineryMapper(Connection connection) {
        this.connection = connection;
    }

    // Vložení nového záznamu do tabulky winery
    public int insert(Winery winery) throws SQLException {
        String sql = "INSERT INTO winery (name, address, website, phone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, winery.getName());
            statement.setString(2, winery.getAddress());
            statement.setString(3, winery.getWebsite());
            statement.setString(4, winery.getPhone());
            statement.executeUpdate();

            // Získání ID nově vloženého záznamu
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    winery.setWineryId(generatedKeys.getInt(1));
                    return winery.getWineryId(); // Vrací ID nově vytvořeného záznamu
                } else {
                    throw new SQLException("Creating winery failed, no ID obtained.");
                }
            }
        }
    }

    // Načtení winery z databáze podle ID
    public Winery findById(int wineryId) throws SQLException {
        String sql = "SELECT * FROM winery WHERE winery_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, wineryId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Winery(
                            resultSet.getInt("winery_id"),
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("website"),
                            resultSet.getString("phone")
                    );
                }
            }
        }
        return null;
    }
}
