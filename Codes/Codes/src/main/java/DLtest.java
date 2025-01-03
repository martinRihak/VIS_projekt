import Tables.Cart;
import Tables.OrdersMapper;
import Tables.WineInCart;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class DLtest {
    public static void main(String[] args) {

       /* try (Connection connection = DBconnection.getConnection()){
            OrdersMapper ordersMapper = new OrdersMapper(connection);
            ordersMapper.findAll();
            ordersMapper.printAllOrders();
            Cart cart = new Cart(1,new BigDecimal("51.5"));
            WineInCart WIC01 = new WineInCart(1,1,100);
            WineInCart WIC02 = new WineInCart(1,2,100);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }
}
