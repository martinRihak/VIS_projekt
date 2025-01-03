import Tables.UserMapper;
import Tables.Users;

import java.sql.Connection;
import java.sql.SQLException;

public class Inherence {
    public static void main(String[] args) {
        try (Connection con = DBconnection.getConnection()) {

            UserMapper userMapper = new UserMapper(con);
            Users.RegUser user = userMapper.findRegUserById(1);
            System.out.println(user);
            System.out.println(user.getLoyalty_points());

            Users user01 = userMapper.findRegUserById(1);
            System.out.println(user01);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
