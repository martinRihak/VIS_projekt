import ORB_folder.WineIdentityMap;
import Tables.Cart;
import Tables.CartMapper;
import Tables.Orders;
import Tables.OrdersMapper;

import java.sql.Connection;
import java.sql.SQLException;

public class RSO {
    public static void main(String[] args) {
        try(Connection connection = DBconnection.getConnection()){
            WineIdentityMap wim = new WineIdentityMap(connection);
            wim.printAllWines();

            CartMapper cartMapper = new CartMapper(connection);
            Cart cart = cartMapper.findById(1);
            System.out.println(cart.getCartId());

            System.out.println(cart.getWinesInCart());

            OrdersMapper ordersMapper = new OrdersMapper(connection);
            Orders orders = ordersMapper.findById(1);
            System.out.println(orders.getOrderItems());

            ordersMapper.delete(orders);

            System.out.println(orders.getOrderItems());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
