package com.moravianwine.app.dto;


import com.moravianwine.app.model.Wine;
import com.moravianwine.app.repository.WineMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Getter
@Component
public class WineIdentityMap {
    private Map<Integer, Wine> identityMap;
    private final WineMapper wineMapper;
    public WineIdentityMap(WineMapper wineMapper) throws SQLException {
        this.wineMapper = wineMapper;
        this.identityMap = new HashMap<>();
    }
    public Map<Integer, Wine> getAllWines() throws SQLException {
        if (identityMap.isEmpty()) {
            identityMap = wineMapper.findAll();
        }
        return identityMap;
    }
    public void addToMap(Wine wine){
        identityMap.put(wine.getWineId(), wine);
    }
    public void loadAllWines() throws SQLException {
        identityMap = wineMapper.findAll();
    }
    public Wine getWine(int wineId) throws SQLException {
        if (identityMap.containsKey(wineId)) {
            return identityMap.get(wineId);
        }
        Wine wine = wineMapper.findById(wineId);
        if (wine != null) {
            identityMap.put(wine.getWineId(), wine);
        }
        throw new RuntimeException("Dane vino neexistuje v databazi");
    }
    public void addWine(Wine wine) {
        if(this.containsWine(wine)) {
            throw new RuntimeException("Dane vino jiz existuje v databazi");
        }
        identityMap.put(wine.getWineId(), wine);
    }

    public boolean containsWine(Wine wine) {
        for(Wine existing : identityMap.values()) {
            if(existing.getName() == wine.getName() &&
            existing.getYear() == wine.getYear() &&
            existing.getWinery().getWineryId() == wine.getWinery().getWineryId()) {return true;}
        }
        return false;
    }
    public void insertWine(Wine wine) throws SQLException {
        wineMapper.insert(wine);
        loadNewWinesFromDatabase();
    }
    public void loadNewWinesFromDatabase() throws SQLException {
        Map<Integer, Wine> newWines = wineMapper.findAll(); // Načte všechna vína z databáze

        for (Wine wine : newWines.values()) {
            if (!identityMap.containsKey(wine.getWineId())) {
                identityMap.put(wine.getWineId(), wine); // Přidá víno do mapy, pokud tam ještě není
            }
        }
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
                    ", Region ID: " + wine.getRegion().getName());
        }
    }

}
