package Tables;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CartMapper {
    private Connection connection;
    private final WineInCartGateway wineInCartGateway;

    public CartMapper(Connection connection) {
        this.connection = connection;
        this.wineInCartGateway = new WineInCartGateway();
    }
    public int insert(Cart cart) throws SQLException {
        String sql = "INSERT INTO cart DEFAULT VALUES"; // Vytvoření nového prázdného košíku
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.executeUpdate();

            // Získání ID nově vytvořeného košíku
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int cartId = generatedKeys.getInt(1);
                    cart.setCartId(cartId); // Nastaví ID do objektu Cart
                    return cartId;
                } else {
                    throw new SQLException("Creating cart failed, no ID obtained.");
                }
            }
        }
    }

    public Cart findById(int cartId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE cart_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cartId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cart cart = new Cart();
                    cart.setCartId(cartId);

                    // Načtení všech vín spojených s tímto košíkem jako Map<Integer, WineInCart>
                    Map<Integer, WineInCart> winesInCart = wineInCartGateway.findWinesInCart(cartId,connection);
                    cart.setWinesInCart(winesInCart);

                    return cart;
                }
            }
        }
        return null;
    }

}
