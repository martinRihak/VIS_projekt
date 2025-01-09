package com.moravianwine.app.service;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Orders;
import com.moravianwine.app.model.Users;
import com.moravianwine.app.repository.OrdersMapper;
import com.moravianwine.app.repository.WineMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;

@Component
public class OrderDomain {
    private final OrdersMapper ordersMapper;
    private final ItemInOrderDomain itemInOrderDomain;
    private final CartDomain cartDomain;
    private final WineMapper wineMapper;

    public OrderDomain(OrdersMapper ordersMapper,WineMapper wp ,ItemInOrderDomain itemInOrderDomain, CartDomain cartDomain) {
        this.ordersMapper = ordersMapper; this.itemInOrderDomain = itemInOrderDomain;
        this.cartDomain = cartDomain;
        this.wineMapper = wp;
    }

    public Orders createOrder(Cart cart, Users user) throws SQLException {

        try{
            ordersMapper.getConnection().setAutoCommit(false);
            Orders order = new Orders();
            order.setTotalPrice(cart.getTotalPrice());
            if (order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SQLException("Total price must be greater than zero");
            }
            order.setStatus('F');
            order.setUserId(user.getUserId());

            Orders newOrder = ordersMapper.insert(order);

            assert itemInOrderDomain != null;
            itemInOrderDomain.insertItems(newOrder,cart);
            wineMapper.updatePriceFromCart(cart);
            ordersMapper.getConnection().commit();
            cartDomain.deleteCartAndItems(cart,user.getUserId());
            System.out.println("Order created");
            return newOrder;
        }catch (SQLException e){
            try{
                ordersMapper.getConnection().rollback();
                throw new IllegalArgumentException(e.getMessage());
            }catch (SQLException rollbackException){
                rollbackException.printStackTrace();
            }
            System.out.println("Chyba pri vkladani Objednavky" + e.getMessage());

            return null;
        }finally {
            try{ordersMapper.getConnection().setAutoCommit(true);}catch (SQLException e){e.printStackTrace();}
        }
    }

}
