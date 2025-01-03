package DomainLogic;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import Tables.*;
public class ReviewTran {
    private final Connection connection;
    private final ReviewgateWay reviewGateway;

    public ReviewTran(Connection connection) {
        this.connection = connection;
        this.reviewGateway = new ReviewgateWay();
    }

    public boolean createReview(int user_id, int wine_id, String reviewText, BigDecimal rating) throws SQLException {
        try {
            // Začátek transakce
            connection.setAutoCommit(false);
            if (reviewGateway.hasReview(connection, user_id, wine_id)) {
                System.out.println("Uživatel již napsal recenzi na toto víno.");
                connection.rollback();
                return false;
            }

            reviewGateway.insertReview(connection, rating, reviewText, user_id, wine_id);

            connection.commit();
            System.out.println("Recenze byla vložena.");
            return true;
        } catch (SQLException e) {
            connection.rollback();
            System.err.println("Chyba při vkládání recenze: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
