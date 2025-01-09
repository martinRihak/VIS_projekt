package com.moravianwine.app.repository;

import com.moravianwine.app.model.Winery;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WineryMapper {
    private final Connection connection;

    public WineryMapper(Connection connection) {
        this.connection = connection;
    }
    public List<Winery> findAll() throws SQLException {
        List<Winery> wineries = new ArrayList<>();
        String sql = "SELECT * FROM winery";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Winery winery = new Winery();
                winery.setWineryId(rs.getInt("winery_id"));
                winery.setName(rs.getString("name"));
                wineries.add(winery);
            }
        }
        return wineries;
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
