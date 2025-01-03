package ORB_folder;

import DataSources.data.Wine;
import DataSources.data.WineMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineUnitOfWork {

    private final Connection connection;
    private final List<Wine> newObjects = new ArrayList<>();
    private final List<Wine> updatedObjects = new ArrayList<>();
    private final List<Wine> deletedObjects = new ArrayList<>();
    private Map<Integer, Wine> identityMap;
    private WineMapper wineMapper;
    public WineUnitOfWork(Connection connection) throws SQLException {
        this.connection = connection;
        this.wineMapper = new WineMapper(connection);
        this.identityMap = wineMapper.findAll();
    }
    public WineUnitOfWork(WineMapper wineMapper,Connection connection) throws SQLException {
        this.connection = connection;
        this.wineMapper = wineMapper;
        this.identityMap = wineMapper.findAll();
    }
    public Wine getWineById(int id){
        if(identityMap.containsKey(id)){
            return identityMap.get(id);
        }
        return null;
    }
    public void addWine(Wine wine) {
        if(identityMap.containsKey(wine.getWineId())){
            updatedObjects.add(wine);
            return;
        }
        newObjects.add(wine);
    }
    public void updateWine(Wine wine) {
        if(!identityMap.containsKey(wine.getWineId())){
            throw new IllegalArgumentException("Wine does not exist");
        }
        updatedObjects.add(wine);
    }
    public void deleteWine(int id) {
        if(!identityMap.containsKey(id)){
            throw new IllegalArgumentException("Wine does not exist");
        }
        deletedObjects.add(identityMap.get(id));
    }
    public void commit() throws SQLException {
        try {
            connection.setAutoCommit(false);

            // Vloží nové objekty
            for (Wine wine : newObjects) {
                wineMapper.insert(wine);
            }
            // Aktualizuje změněné objekty
            for (Wine wine : updatedObjects) {
                wineMapper.update(wine);
            }
            // Smaže odstraněné objekty
            for (Wine wine : deletedObjects) {
                wineMapper.delete(wine);
            }
            connection.commit();
            System.out.println("Všechny změny byly úspěšně potvrzeny.");
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Chyba při potvrzování transakce: " + e.getMessage());
            throw e;
        } finally {
            connection.setAutoCommit(true);
            clear();
        }
    }
    private void clear() {
        newObjects.clear();
        updatedObjects.clear();
        deletedObjects.clear();
    }


}
