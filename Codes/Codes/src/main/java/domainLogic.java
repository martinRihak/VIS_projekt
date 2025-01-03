import DomainLogic.ReviewDomain;
import DomainLogic.ReviewTran;
import Tables.Users;
import Tables.Wine;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class domainLogic {
    public static void main(String[] args) {
        try (Connection connection = DBconnection.getConnection()){
            ReviewTran reviewTran = new ReviewTran(connection);

            reviewTran.createReview(1,8,"Hodnotim velmi kladne",new BigDecimal(4));

            ReviewDomain reviewDomain = new ReviewDomain(connection);
            Wine wine = new Wine();
            wine.setWineId(1);
            Users.RegUser user = new Users.RegUser();
            user.setUserId(1);
            Wine wine02 = new Wine();
            wine02.setWineId(5);
            reviewDomain.createReview(connection,user,wine,"Nic moc",new BigDecimal(1));

            reviewDomain.createReview(connection,user,wine02,"Nejlepsi vino z teto odrudy, ktere jsem kdy mel",new BigDecimal(5));


        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }
}
