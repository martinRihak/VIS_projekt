package com.moravianwine.app.dto;


import com.moravianwine.app.model.Wine;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@Data
@AllArgsConstructor
public class WineUnitOfWork {
    private final Connection connection;
    private final List<Wine> newObjects = new ArrayList<>();
    private final List<Wine> updatedObjects = new ArrayList<>();
    private final List<Wine> deletedObjects = new ArrayList<>();
    private WineIdentityMap wineIdentityMap;

    public Wine getWineById(int id){
        if(wineIdentityMap.getIdentityMap().containsKey(id)){
            return wineIdentityMap.getIdentityMap().get(id);
        }
        return null;
    }


    public void addWine(Wine wine) {
        for(Wine existing : wineIdentityMap.getIdentityMap().values()) {
            if(existing.getName() == wine.getName() &&
                    existing.getYear() == wine.getYear() &&
                    existing.getWinery().getWineryId() == wine.getWinery().getWineryId()) {
                updatedObjects.add(wine);
                return;
            }
        }
        newObjects.add(wine);
    }
    public void updateWine(Wine wine) {
        for(Wine existing : wineIdentityMap.getIdentityMap().values()) {
            if(existing.getName() != wine.getName() &&
                    existing.getYear() != wine.getYear() &&
                    existing.getWinery().getWineryId() != wine.getWinery().getWineryId()) {
                throw new IllegalArgumentException("Wine does not exist");
            }
        }
        updatedObjects.add(wine);
    }
    public void deleteWine(int id) {
        if(!wineIdentityMap.getIdentityMap().containsKey(id)){
            throw new IllegalArgumentException("Wine does not exist");
        }
        deletedObjects.add(wineIdentityMap.getIdentityMap().get(id));
    }
    public void commit() throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Vloží nové objekty
            for (Wine wine : newObjects) {
                wineIdentityMap.getWineMapper().insert(wine);
            }
            // Aktualizuje změněné objekty
            for (Wine wine : updatedObjects) {
                wineIdentityMap.getWineMapper().update(wine);
            }
            // Smaže odstraněné objekty
            for (Wine wine : deletedObjects) {
                wineIdentityMap.getWineMapper().delete(wine);
            }
            connection.commit();
            System.out.println("Všechny změny byly úspěšně potvrzeny.");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Chyba při potvrzování transakce: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);
            wineIdentityMap.loadNewWinesFromDatabase();
        }
    }
    private void clear() {
        newObjects.clear();
        updatedObjects.clear();
        deletedObjects.clear();
    }


}
