package Tables;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data @Getter @Setter
@AllArgsConstructor
public class Users {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private char role;
    private java.sql.Timestamp createAt;
    private String address;
    private String country;

    public Users() {
        this.createAt = new java.sql.Timestamp(System.currentTimeMillis());
    }
    public Users(ResultSet rs) throws SQLException {
        this.userId = rs.getInt("user_id");
        this.username = rs.getString("username");
        this.firstName = rs.getString("firstName");
        this.lastName = rs.getString("lastName");
        this.email = rs.getString("email");
        this.passwordHash = rs.getString("password_hash");
        this.role = rs.getString("role").charAt(0); // Předpokládá, že role je CHAR v databázi
        this.createAt = rs.getTimestamp("create_at");
        this.address = rs.getString("address");
        this.country = rs.getString("country");
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SysAdmin extends Users{
        private String oddeleni;

        public SysAdmin(ResultSet rs) throws SQLException {
            super(rs);
            oddeleni = rs.getString("oddeleni");

        }
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegUser extends Users{
        private List<Orders> orders;
        private int loyalty_points;

        public RegUser(ResultSet rs) throws SQLException {
            super(rs);
            this.loyalty_points = rs.getInt("loyalty_points");
            this.orders = new ArrayList<>();
        }
    }


    public Review addReview(Connection connection, BigDecimal rating, String reviewText, Wine wine, ReviewgateWay reviewRepository) throws Exception {
        if (reviewRepository.hasReview(connection,this, wine)) {
            throw new Exception("Uživatel již napsal recenzi na toto víno.");
        }
        return new Review(rating,reviewText,this.userId,wine.getWineId());
    }


}
