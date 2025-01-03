import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import DataSources.row.WineRowGateway;
import DataSources.table.WineTableGateway;
import DataSources.active.Wine;
import DataSources.data.*;

public class cvi03 {
    public static void main(String[] args) {
        try (Connection connection = DBconnection.getConnection()){

            WineTableGateway TableGate = new WineTableGateway(connection);
            TableGate.getAllWines();

            System.out.println(TableGate.getAllWines());
            WineRowGateway RowGate = new WineRowGateway(connection,7);

            System.out.println(RowGate.getName());

            Wine wineRecord = Wine.findById(connection,7);
            System.out.println(wineRecord.getName());

            WineMapper wineMapper = new WineMapper(connection);

            wineMapper.findById(7).getName();
            System.out.println(wineMapper.findById(7).getName());

            Map<Integer,DataSources.data.Wine> wineList = wineMapper.findAll();
            System.out.println(wineList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
