package com.moravianwine.app.repository;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.model.WineInCart;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
@Component
public class WineInCartGateway {

    public void insert(Connection connection, WineInCart item) throws SQLException {
            String sql = "insert into wine_in_cart(cart_id,wine_id,quantity) values(?,?,?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, item.getCart().getCartId());
                preparedStatement.setInt(2, item.getWine().getWineId());
                preparedStatement.setInt(3, item.getQuantity());
                preparedStatement.executeUpdate();
            }
    }
    public void update(Connection connection, int cart_id, int wine_id, int quantity) throws SQLException {
        int newQuantity = getQuantity(connection, cart_id, wine_id);
        String sql = "UPDATE wine_in_cart SET quantity=? WHERE cart_id=? AND wine_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, quantity + newQuantity);
            preparedStatement.setInt(2, cart_id);
            preparedStatement.setInt(3, wine_id);
            preparedStatement.executeUpdate();
        }
    }
    public void delete(Connection connection, int cart_id, int wine_id) throws SQLException {
        String sql = "DELETE from wine_in_cart WHERE cart_id=? AND wine_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, cart_id);
            preparedStatement.setInt(2, wine_id);
            preparedStatement.executeUpdate();
        }
    }
    public int getQuantity(Connection connection, int cart_id, int wine_id) throws SQLException {
        String sql = "SELECT w.quantity FROM wine_in_cart w WHERE cart_id = ? AND wine_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cart_id);
            statement.setInt(2, wine_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity");
                } else {
                    throw new SQLException("Quantity not found for cart_id: " + cart_id + " and wine_id: " + wine_id);
                }
            }
        }
    }
    public boolean findById(Connection connection, int cart_id, int wine_id) throws SQLException {
        String sql = "SELECT * FROM wine_in_cart WHERE cart_id=? AND wine_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, cart_id);
            preparedStatement.setInt(2, wine_id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
            }
        }
        return false;
    }
    public Map<Integer, WineInCart> findWinesInCart(Cart cartId,Connection connection) throws SQLException {
        Map<Integer, WineInCart> winesInCart = new HashMap<>();
        String sql = "SELECT * FROM wine_in_cart WHERE cart_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartId.getCartId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int wineId = resultSet.getInt("wine_id");
                    int quantity = resultSet.getInt("quantity");


                    Wine wine = new WineMapper(connection).findById(wineId);


                    WineInCart wineInCart = new WineInCart(cartId, wine, quantity);
                    winesInCart.put(wineId, wineInCart);
                }
            }
        }
        return winesInCart;
    }
}
