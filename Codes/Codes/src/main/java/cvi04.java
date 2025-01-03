/*  package tests;

import DataSources.data.Wine;
import DataSources.data.WineMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class cvi04 {
    public static void test(String[] args) {

        try (Connection connection = DBconnection.getConnection()){
            WineMapper wineMapper = new WineMapper(connection);
            WineUnitOfWork unitOfWork = new WineUnitOfWork(connection);
            List<Wine> wines = wineMapper.findAll();

            Wine newWine1 =new Wine(158,"Merlot","Vino Vylet",20.99);
            unitOfWork.addNewWine(newWine1);

            Wine existingWine = new Wine(1, "Merlot", "Oregon Winery", 20.99);
            unitOfWork.addDirtyWine(existingWine);

            unitOfWork.commitWines(wineMapper);

            wines = wineMapper.findAll();
            wines.forEach(System.out::println);

        }catch (SQLException e){

        }
    }
}*/
