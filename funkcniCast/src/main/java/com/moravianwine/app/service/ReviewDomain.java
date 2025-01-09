package com.moravianwine.app.service;

import com.moravianwine.app.model.Review;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.repository.ReviewgateWay;

import java.math.BigDecimal;
import java.sql.SQLException;

public class ReviewDomain {
    private final ReviewgateWay reviewGateway;

    public ReviewDomain(ReviewgateWay reviewGateway) {
        this.reviewGateway = reviewGateway;
    }
    public boolean createReview(Users user, Wine wine, String reviewText, BigDecimal rating) {
        try {
            reviewGateway.getConnection().setAutoCommit(false);

            Review review = user.addReview(reviewGateway.getConnection(),rating,reviewText,wine,reviewGateway);
            System.out.println(review);
            reviewGateway.insertReview(review);

            reviewGateway.getConnection().commit();
            System.out.println("Recenze byla vložena.");
            return true;
        } catch (SQLException e) {
            try {
                reviewGateway.getConnection().rollback();
                throw new IllegalArgumentException(e.getMessage());
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            System.out.println("Chyba při vkládání recenze: " + e.getMessage());

            return false;
        } finally {
            try {
                reviewGateway.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
