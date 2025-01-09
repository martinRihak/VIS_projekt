package com.moravianwine.app.repository;

import com.moravianwine.app.model.Region;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class RegionGateWay {
    private Connection connection;
    public RegionGateWay(Connection connection) {
        this.connection = connection;
    }

    public List<Region> findAll() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM region";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Region region = new Region();
                region.setRegionId(rs.getInt("region_id"));
                region.setName(rs.getString("name"));
                region.setCountry(rs.getString("country"));
                regions.add(region);
            }
        }
        return regions;
    }
    public Region findById(int id) throws SQLException {
        String sql = "SELECT * FROM region WHERE region_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Region region = new Region();
                    region.setRegionId(rs.getInt("region_id"));
                    region.setName(rs.getString("name"));
                    region.setCountry(rs.getString("country"));
                    return region;
                }
            }
        }
        return null;
    }

}
