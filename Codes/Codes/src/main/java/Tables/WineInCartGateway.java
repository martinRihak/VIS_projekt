package Tables;

import DataSources.data.Wine;
import DataSources.data.WineMapper;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WineInCartGateway {

    public void insert(Connection connection, int cart_id, int wine_id, int quantity) throws SQLException {
            String sql = "insert into wineInCart(cart_id,wine_id,quantity) values(?,?,?,?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, cart_id);
                preparedStatement.setInt(2, wine_id);
                preparedStatement.setInt(3, quantity);
                preparedStatement.executeUpdate();
            }
    }
    public void update(Connection connection, int cart_id, int wine_id, int quantity) throws SQLException {
        String sql = "UPDATE wineIncart SET quantity=? WHERE cart_id=? AND wine_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, quantity);
            preparedStatement.executeUpdate();
        }
    }
    public void delete(Connection connection, int cart_id, int wine_id) throws SQLException {
        String sql = "DELETE from wineInCart WHERE cart_id=? AND wine_id=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, cart_id);
            preparedStatement.setInt(2, wine_id);
            preparedStatement.executeUpdate();
        }
    }
    // Načtení všech vín v konkrétním košíku
    public Map<Integer, WineInCart> findWinesInCart(int cartId,Connection connection) throws SQLException {
        Map<Integer, WineInCart> winesInCart = new HashMap<>();
        String sql = "SELECT * FROM wineInCart WHERE cart_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int wineId = resultSet.getInt("wine_id");
                    int quantity = resultSet.getInt("quantity");


                    Wine wine = new WineMapper(connection).findById(wineId);
                    Cart cart = new Cart();  // Cart je vytvořen pro odkaz
                    cart.setCartId(cartId);

                    WineInCart wineInCart = new WineInCart(cart, wine, quantity);
                    winesInCart.put(wineId, wineInCart);
                }
            }
        }
        return winesInCart;
    }
}
