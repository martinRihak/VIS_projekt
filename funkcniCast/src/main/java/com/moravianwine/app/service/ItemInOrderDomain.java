package com.moravianwine.app.service;

import com.moravianwine.app.model.Cart;
import com.moravianwine.app.model.Orders;
import com.moravianwine.app.repository.ItInOrTaGate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class ItemInOrderDomain {
    private final ItInOrTaGate itInOrTaGate;
    private final Connection connection;
    public ItemInOrderDomain(ItInOrTaGate itInOrTaGate,Connection connection) {
        this.itInOrTaGate = itInOrTaGate;
        this.connection = connection;
    }
    public void insertItems(Orders order, Cart cart) throws SQLException {
        try {
            cart.getWinesInCart().values().forEach(wine -> {
                try {
                    itInOrTaGate.insert(connection,wine.getQuantity(), BigDecimal.valueOf(wine.getWine().getPrice()), order.getOrderId(), wine.getWine().getWineId());
                } catch (SQLException e) {
                    throw new RuntimeException("Chyba při vkládání vína do objednávky", e);
                }
            });
            System.out.println("Vsechny polozky byly vlozeny");
            connection.commit();
        } catch (SQLException e) {
            try{
                connection.rollback();
                throw new IllegalArgumentException(e.getMessage());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

}
