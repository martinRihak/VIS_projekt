package com.moravianwine.app.repository;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.WineInCart;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
@Component @Getter
public class CartMapper {
    private Connection connection;
    private final WineInCartGateway wineInCartGateway;

    public CartMapper(Connection connection) {
        this.connection = connection;
        this.wineInCartGateway = new WineInCartGateway();
    }
    public int insert(int user_id) throws SQLException {
        String sql = "INSERT INTO cart (user_id, total_price) VALUES (?, 0.00)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, user_id);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Vypíše podrobnou chybu
            throw new SQLException("Chyba při vkládání záznamu: " + e.getMessage());
        }
        throw new SQLException("Vložení košíku selhalo, nebylo vygenerováno žádné ID.");
    }
    public void delete(int user_id) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            statement.executeUpdate();
        } catch (SQLException e) {

            throw new SQLException("Chyba při mazani: " + e.getMessage());
        }
    }
    public Cart findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int cartId = resultSet.getInt("cart_id");
                    BigDecimal price = resultSet.getBigDecimal("total_price");
                    Cart cart = new Cart();
                    cart.setCartId(cartId);
                    cart.setTotalPrice(price);
                    Map<Integer, WineInCart> winesInCart = wineInCartGateway.findWinesInCart(cart,connection);
                    cart.setWinesInCart(winesInCart);
                    return cart;
                }
            }
        }

        return null;
    }
    public Cart findById(int cartId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE cart_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(cartId);

                    Map<Integer, WineInCart> winesInCart = wineInCartGateway.findWinesInCart(cart,connection);
                    cart.setWinesInCart(winesInCart);

                    return cart;
                }
            }
        }
        return null;
    }
    public void updateCartTotalPrice(Cart cart) throws SQLException {
        String sql = "UPDATE cart SET total_price = ? WHERE cart_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, cart.getTotalPrice());
            statement.setInt(2, cart.getCartId());
            statement.executeUpdate();
        }
    }
}
