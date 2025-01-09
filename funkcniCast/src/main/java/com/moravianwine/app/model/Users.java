package com.moravianwine.app.model;

import com.moravianwine.app.repository.ReviewgateWay;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Entity
@Data @Getter @Setter
@AllArgsConstructor
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private char role;
    private java.sql.Timestamp createAt;
    @Transient
    private Address address;
    @Transient
    private Cart cart;
    public Users() {
        this.createAt = new java.sql.Timestamp(System.currentTimeMillis());
    }
    public Users(ResultSet rs) throws SQLException {
        this.userId = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.firstName = rs.getString("first_name");
        this.lastName = rs.getString("last_name");
        this.email = rs.getString("email");
        this.passwordHash = rs.getString("password_hash");
        this.role = rs.getString("role").charAt(0); // Předpokládá, že role je CHAR v databázi
        this.createAt = rs.getTimestamp("create_at");
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SysAdmin extends Users {
        private String oddeleni;

        public SysAdmin(ResultSet rs) throws SQLException {
            super(rs);
            oddeleni = rs.getString("oddeleni");

        }
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegUser extends Users {
        private int loyalty_points;
        public RegUser(ResultSet rs) throws SQLException {
            super(rs);
            this.loyalty_points = rs.getInt("loyalty_points");
        }
    }


    public Review addReview(Connection connection, BigDecimal rating, String comment, Wine wine, ReviewgateWay reviewRepository) throws SQLException {
        if (reviewRepository.hasReview(connection,this, wine)) {
            throw new SQLException("Uživatel již napsal recenzi na toto víno.");
        }

        return new Review(wine,this,rating,comment);
    }
    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address{
        private String address;
        private String country;
        private String ZIP;
    }

}