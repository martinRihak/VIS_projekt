import DataSources.data.Wine;
import DataSources.data.WineMapper;
import ORB_folder.WineIdentityMap;
import ORB_folder.WineUnitOfWork;
import Tables.ReviewgateWay;
import Tables.Winery;
import Tables.WineryMapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class ORB {
    public static void main(String[] args) {
        try (Connection connection = DBconnection.getConnection()) {
           /* WineIdentityMap wim = new WineIdentityMap(connection);

            wim.printAllWines();

            System.out.println(wim.getWine(10));

            WineUnitOfWork wuow = new WineUnitOfWork(connection);
            WineryMapper wineryMapper =  new WineryMapper(connection);
            Winery newWinery = wineryMapper.findById(1);
            Wine newWine = new Wine (11,"Merlot Classic", "1", 2023, 250.00, "Hladký a bohatý Merlot s chutěmi třešní a čokolády.", 50, newWinery, 1);
            Wine updateWine = wuow.getWineById(5);
            updateWine.setYear(2010);
            wuow.addWine(newWine);
            wuow.addWine(updateWine);

           /* for (int i = 19; i < 25; i++) {
                wuow.deleteWine(i);
            }
            wuow.commit();
            */
            ReviewgateWay reviewGateway = new ReviewgateWay();
            Wine newWine02 = new WineMapper(connection).findById(7);
            System.out.println(newWine02);
            newWine02.addReviewHolder(reviewGateway.createReviewLoader(connection, newWine02.getWineId()));

            System.out.println(newWine02.getReviews());


            newWine02.setReviewList(new WineMapper(connection).getReviews(newWine02,reviewGateway));
            System.out.println(newWine02.getReviewList());
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
