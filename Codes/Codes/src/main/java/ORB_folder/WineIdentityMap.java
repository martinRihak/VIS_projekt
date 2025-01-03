package ORB_folder;

import DataSources.data.Wine;
import DataSources.data.WineMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class WineIdentityMap {
    private Map<Integer, Wine> identityMap;
    private WineMapper wineMapper;
    public WineIdentityMap(Connection connection) throws SQLException {
        this.wineMapper = new WineMapper(connection);
        this.identityMap = wineMapper.findAll();
    }
    public WineIdentityMap(WineMapper wineMapper) throws SQLException {
        this.wineMapper = wineMapper;
        this.identityMap = wineMapper.findAll();
    }

    public Wine getWine(int wineId) throws SQLException {
        if (containsWine(wineId)) {
            return identityMap.get(wineId);
        }
        Wine wine = wineMapper.findById(wineId);
        if (wine != null) {
            identityMap.put(wine.getWineId(), wine);
        }
        throw new RuntimeException("Dane vino neexistuje v databazi");
    }

    public void addWine(Wine wine) {
        if(this.containsWine(wine.getWineId())) {
            throw new RuntimeException("Dane vino jiz existuje v databazi");
        }
        identityMap.put(wine.getWineId(), wine);
    }

    public boolean containsWine(int wineId) {
        return identityMap.containsKey(wineId);
    }
    public void printAllWines() {
        if (identityMap.isEmpty()) {
            System.out.println("No wines in the identity map.");
            return;
        }

        for (Wine wine : identityMap.values()) {
            System.out.println("Wine ID: " + wine.getWineId() +
                    ", Name: " + wine.getName() +
                    ", Type: " + wine.getType() +
                    ", Year: " + wine.getYear() +
                    ", Price: " + wine.getPrice() +
                    ", Description: " + wine.getDescription() +
                    ", Stock Quantity: " + wine.getStockQuantity() +
                    ", Winery ID: " + wine.getWinery().getName() +
                    ", Region ID: " + wine.getRegionId());
        }
    }

}
