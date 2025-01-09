package com.moravianwine.app.service;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Wine;
import com.moravianwine.app.model.WineInCart;
import com.moravianwine.app.repository.CartMapper;
import com.moravianwine.app.repository.WineInCartGateway;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Getter @Setter
@AllArgsConstructor
@Component
public class CartDomain {
    private final Connection connection;
    private final WineInCartGateway wineInCartGateway;
    private final CartMapper cartMapper;
    public void addWineToCart(Cart cart, Wine wine, int quantity)throws SQLException {
        try{
            connection.setAutoCommit(false);
            boolean hasWine = wineInCartGateway.findById(connection, cart.getCartId(), wine.getWineId());
            if (!hasWine) {
                WineInCart wineInCart = new WineInCart();
                wineInCart.setCart(cart);
                wineInCart.setWine(wine);
                wineInCart.setQuantity(quantity);

                wineInCartGateway.insert(connection,wineInCart);
            }else {
                wineInCartGateway.update(connection, cart.getCartId(), wine.getWineId(), quantity);
            }
            BigDecimal totalItemPrice = BigDecimal.valueOf(wine.getPrice()).multiply(BigDecimal.valueOf(quantity));
            BigDecimal updatedTotalPrice = cart.getTotalPrice().add(totalItemPrice);
            cart.setTotalPrice(updatedTotalPrice);
            cartMapper.updateCartTotalPrice(cart);
        }catch (SQLException e){
            connection.rollback();
            throw new SQLException("Transakce neprobehla: " + e.getMessage());
        }finally {
            connection.setAutoCommit(true);
        }
    }
    public Cart findOrCreateCart(int userId) throws SQLException {
        Cart cart = cartMapper.findByUserId(userId);
        if(cart == null){
            int cartId = cartMapper.insert(userId);
            cart = new Cart();
            cart.setCartId(cartId);
            cart.setTotalPrice(new BigDecimal("0.0"));
            Map<Integer, WineInCart> winesInCart = wineInCartGateway.findWinesInCart(cart,connection);
            cart.setWinesInCart(winesInCart);
        }
        return cart;
    }
    public void deleteCartAndItems(Cart cart,int userId) throws SQLException {
        try {
            connection.setAutoCommit(false);
            cart.getWinesInCart().values().forEach( wineInCart -> {
                try {
                    wineInCartGateway.delete(connection,wineInCart.getCart().getCartId(),wineInCart.getWine().getWineId());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            cartMapper.delete(userId);
            cartMapper.getConnection().commit();
        }catch (SQLException e){
            cartMapper.getConnection().rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

}
