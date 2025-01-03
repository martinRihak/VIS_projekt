package DomainLogic;

import Tables.Review;
import Tables.ReviewgateWay;
import Tables.Users;
import Tables.Wine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class ReviewDomain {
    private final Connection connection;
    private final ReviewgateWay reviewGateway;

    public ReviewDomain(Connection connection) {
        this.connection = connection;
        this.reviewGateway = new ReviewgateWay();
    }
    public boolean createReview(Connection connection, Users user, Wine wine, String reviewText, BigDecimal rating) {
        try {
            connection.setAutoCommit(false);

            Review review = user.addReview(connection,rating,reviewText,wine,reviewGateway);

            reviewGateway.insertReview(connection,review);

            connection.commit();
            System.out.println("Recenze byla vložena.");
            return true;
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            System.err.println("Chyba při vkládání recenze: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
