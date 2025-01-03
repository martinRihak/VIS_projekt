package Tables;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Cart {
    private int cartId;
    private BigDecimal totalPrice;
    private Map<Integer,WineInCart> winesInCart;

}

