package com.moravianwine.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ItemInOrder {
    private int itemOrderId;
    private int quantity;
    private BigDecimal pricePerUnit;
    private int orderId;
    private int wineId;

}
