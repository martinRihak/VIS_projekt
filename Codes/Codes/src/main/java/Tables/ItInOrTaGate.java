package Tables;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ItInOrTaGate {

    public ItInOrTaGate() {}
    public void insert(Connection conn, int quantity, BigDecimal price_per_unit, int order_id, int wine_id) throws SQLException {
        String sql = "INSERT INTO item_in_order (quantity,price_per_unit,order_id,wine_id) VALUES (?,?,?,?)";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, quantity);
            statement.setBigDecimal(2, price_per_unit);
            statement.setInt(3, order_id);
            statement.setInt(4, wine_id);
            statement.executeUpdate();
        }
    }
    public void delete(Connection conn, int order_id) throws SQLException {
        String sql = "DELETE FROM item_in_order WHERE order_id = ?";
        try(PreparedStatement statement = conn.prepareStatement(sql)){
            statement.setInt(1, order_id);
            statement.executeUpdate();
        }
    }
    public Map<Integer,ItemInOrder> getAllItems(Connection connection,int order_id) throws SQLException{
        Map<Integer,ItemInOrder> items = new HashMap<Integer,ItemInOrder>();
        String sql = "SELECT * FROM item_in_order WHERE order_id = ?";

        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, order_id);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    int item_id = resultSet.getInt("item_order_id");
                    int quantity = resultSet.getInt("quantity");
                    BigDecimal price_per_unit = resultSet.getBigDecimal("price_per_unit");
                    int wine_id = resultSet.getInt("wine_id");
                    ItemInOrder item = new ItemInOrder(item_id,quantity,price_per_unit,order_id,wine_id);

                    items.put(item_id,item);

                }
            }
        }
        return items;
    }
}
