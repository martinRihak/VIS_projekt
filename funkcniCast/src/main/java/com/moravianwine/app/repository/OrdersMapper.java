package com.moravianwine.app.repository;

import com.moravianwine.app.model.Orders;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component @Getter
public class OrdersMapper {
    private Connection connection;

    public OrdersMapper(Connection connection) {
        this.connection = connection;
    }

    public Orders insert(Orders order) throws SQLException {
        String sql = "INSERT INTO orders (total_price, status, user_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, order.getTotalPrice());
            statement.setString(2, String.valueOf(order.getStatus()));
            statement.setInt(3, order.getUserId());
            statement.executeUpdate();


            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
            }
        }
        return order;
    }
    public void update(Orders order) throws SQLException {
        String sql = "UPDATE orders SET total_price = ?, status = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, order.getTotalPrice());
            statement.setString(2, String.valueOf(order.getStatus()));
            statement.setInt(3, order.getOrderId());
            statement.executeUpdate();
        }
    }

    public void delete(Orders order) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        ItInOrTaGate itemOderder = new ItInOrTaGate();
        itemOderder.delete(connection, order.getOrderId());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, order.getOrderId());
            statement.executeUpdate();
        }
    }

    public Orders findById(int orderId) throws SQLException {
        ItInOrTaGate itemGate = new ItInOrTaGate();
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Orders order = new Orders();
                            order.setOrderId(resultSet.getInt("order_id"));
                            order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                            order.setStatus(resultSet.getString("status").charAt(0));
                            order.setCreatedAt(resultSet.getTimestamp("created_at"));
                            order.setUserId(resultSet.getInt("user_id"));
                            order.setOrderItems(itemGate.getAllItems(connection,orderId));
                    return order;
                }
            }
        }
        return null;
    }
    public List<Orders> findAll() throws SQLException {
        List<Orders> orderList = new ArrayList<>();

        String sql = "SELECT * FROM orders";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Orders order = new Orders();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                order.setStatus(resultSet.getString("status").charAt(0));
                order.setCreatedAt(resultSet.getTimestamp("created_at"));
                order.setUserId(resultSet.getInt("user_id"));

                orderList.add(order);
            }
        }
        return orderList;
    }
    public void printAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                BigDecimal totalPrice = resultSet.getBigDecimal("total_price");
                char status = resultSet.getString("status").charAt(0);
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                int userId = resultSet.getInt("user_id");

                System.out.println("Order ID: " + orderId +
                        ", Total Price: " + totalPrice +
                        ", Status: " + status +
                        ", Created At: " + createdAt +
                        ", User ID: " + userId);
            }
        }
    }
}
