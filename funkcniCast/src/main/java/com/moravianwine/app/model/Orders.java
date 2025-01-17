package com.moravianwine.app.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private int orderId;
    private BigDecimal totalPrice;
    private char status;
    private Timestamp createdAt;
    private int userId;
    private  Map<Integer,ItemInOrder> orderItems;
}

